/*
 * Copyright 2009 Junpei Kawamoto
 *
 * This file is part of Japanese WordNet wrapper for Java (Wnj2).
 * Wnj2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Wnj2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Wnj2.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.wnj2;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * データベースへの接続を管理する基本オブジェクト．
 *
 * <p>
 * Wnj2ライブラリを使用する場合，始めにこのオブジェクトを作成する必要があります．
 * Wnj2インスタンスの作成方法は，日本語WordNetのデータファイルを指定する方法とデータベースへのコネクションを指定する方法の二種類があります．
 * </p>
 *
 * <p>
 * データファイルを指定して作成する場合，日本語WordNetウェブサイトで配布されているSQLite用データファイルをコンストラクタに指定します．
 * </p>
 * <blockquote><pre>
 * File file = new File("path_to_data_file");
 * Wnj2 wn = new Wnj2(file);
 * </pre></blockquote>
 * <p>
 * この方法を使用する場合，SQLite用JDBCドライバが必要になります．
 * </p>
 *
 * <p>
 * データベースコネクションを指定して作成する方法は，主にSQLite以外のDBMSにデータを格納している場合に用います．
 * 配布されているSQLite用データを他のDBMSへ移行するツールは
 * <a href="http://sourceforge.jp/projects/wnj2/releases/">こちら</a>
 * で配布予定です．
 * 例えばMySQLを使用する場合は次のようになるでしょう（環境によって変わります）．
 * </p>
 * <blockquote><pre>
 * Class.forName("com.mysql.jdbc.Driver");
 * Connection con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/wnj", "wnj", "pass2wnj");
 *
 * Wnj2 wn = new Wnj2(con);
 * </pre></blockquote>
 *
 * <p>
 * どちらの方法で作成した場合も，使用後はcloseメソッドを呼び出して下さい．データベースへの接続を切ります．
 * </p>
 *
 *  @see Word
 *  @see Synset
 *
 */
public class Wnj2 implements Closeable{

	private final Connection connection;

	private static final String FIND_WORD_BY_LEMMA = "select * from WORD where lemma = ?;";
	private static final String FIND_WORD_BY_LEMMA_AND_POS = "select * from WORD where lemma = ? and pos = ?;";
	private static final String FIND_WORD_BY_WORDID = "select * from WORD where wordid = ?;";

	private static final String FIND_SENSES_BY_WORDID = "select * from SENSE where wordid = ?";
	private static final String FIND_SENSES_BY_SYNSET = "select * from SENSE where synset = ?";
	private static final String FIND_SENSES_BY_SYNSET_AND_LANG = "select * from SENSE where synset = ? and lang = ?";

	private static final String FIND_SYNSETS_BY_NAME_AND_POS = "select * from SYNSET where name = ? and pos = ?;";
	private static final String FIND_SYNSET_BY_SYNSET = "select * from SYNSET where synset = ?;";
	private static final String FIND_SYNSET_BY_SYNSET_AND_LINK = "select s.synset, s.pos, s.name, s.src from SYNLINK l inner join SYNSET s on l.synset2 = s.synset where l.synset1 = ?;";

	private static final String FIND_SYNSETDEF_BY_SYNSET = "select * from SYNSET_DEF where synset = ?";

	private static final String FIND_SYNLINK_BY_SYNSET = "select * from SYNLINK where synset1 = ?;";
	private static final String FIND_SYNLINK_BY_SYNSET_AND_LINK = "select * from SYNLINK where synset1 = ? and link = ?";

	private Reference<PreparedStatement> findWordsByLemma = new SoftReference<PreparedStatement>(null);
	private Reference<PreparedStatement> findWordsByLemmaAndPos = new SoftReference<PreparedStatement>(null);
	private Reference<PreparedStatement> findWordByWordid = new SoftReference<PreparedStatement>(null);

	private Reference<PreparedStatement> findSensesByWordid = new SoftReference<PreparedStatement>(null);
	private Reference<PreparedStatement> findSensesBySynset = new SoftReference<PreparedStatement>(null);
	private Reference<PreparedStatement> findSensesBySynsetAndLang = new SoftReference<PreparedStatement>(null);

	private Reference<PreparedStatement> findSynsetsByNameAndPos = new SoftReference<PreparedStatement>(null);
	private Reference<PreparedStatement> findSynsetBySynset = new SoftReference<PreparedStatement>(null);
	private Reference<PreparedStatement> findSynsetBySynsetAndLink = new SoftReference<PreparedStatement>(null);

	private Reference<PreparedStatement> findSynsetDefBySynset = new SoftReference<PreparedStatement>(null);

	private Reference<PreparedStatement> findSynlinksBySynset = new SoftReference<PreparedStatement>(null);
	private Reference<PreparedStatement> findSynlinksBySynsetAndLink = new SoftReference<PreparedStatement>(null);

	/////////////////////////////////////////////////////////////////////////////////////
	// Constractors
	/////////////////////////////////////////////////////////////////////////////////////
	/**
	 * 指定したSQLiteデータベースファイルへアクセスするWnj2インスタンスを作成する．
	 *
	 * @param file 日本語WordNetデータベースファイル
	 *
	 * @throws ClassNotFoundException SQLite用JDBSドライバの読み込みに失敗した場合
	 * @throws IOException 日本語WordNetデータベースファイルに関する入出力エラーが発生した場合
	 */
	public Wnj2(final File file) throws ClassNotFoundException, IOException{
		assert file != null : "file is null";

		if(!file.exists()){

			throw new FileNotFoundException(file.toString());

		}

		Class.forName("org.sqlite.JDBC");
		try{

			this.connection = DriverManager.getConnection(String.format("jdbc:sqlite:%s", file.getAbsolutePath()));

		}catch(final SQLException e){

			throw new IOException(String.format("%sに関する入出力エラー", file));

		}

	}

	/**
	 * データベースへのコネクションを指定してWnj2インスタンスを作成する．
	 *
	 * @param connection 日本語WordNetデータベースへ接続済みのコネクション
	 */
	public Wnj2(final Connection connection){
		assert connection != null : "connection is null";

		this.connection = connection;

	}

	/////////////////////////////////////////////////////////////////////////////////////
	// Public methods
	/////////////////////////////////////////////////////////////////////////////////////
	/**
	 * データベースへの接続を切る．
	 *
	 * コネクションを指定してこのオブジェクトを作成した場合，そのコネクションも閉じます．
	 * @throws IOException 入出力エラーが発生した場合
	 */
	@Override
	public void close() throws IOException {

		try{

			this.connection.close();

		}catch(final SQLException e){

			throw new IOException(e.getMessage());

		}

	}

	/**
	 * 見出し語に一致するWord集合を取得する．
	 *
	 * @param lemma 見出し語
	 * @return 見出し語lemmaに一致するWordのリスト
	 */
	public List<Word> findWords(final String lemma){
		assert lemma != null : "lemma is null";

		PreparedStatement ps = this.findWordsByLemma.get();
		if(ps == null){

			ps = this.createPreparedStatement(FIND_WORD_BY_LEMMA);
			this.findWordsByLemma = new SoftReference<PreparedStatement>(ps);

		}

		final List<Word> ret = new ArrayList<Word>();
		try {

			ps.setString(1, lemma.toLowerCase());
			ret.addAll(this.createWords(ps));

		}catch(final SQLException e){

			e.printStackTrace();

		}

		return ret;

	}

	/**
	 * 見出し語と品詞を指定して一致するWord集合を取得する．
	 *
	 * @param lemma 見出し語
	 * @param pos 品詞
	 * @return 見出し語lemmaと品詞posに一致するWordのリスト
	 */
	public List<Word> findWords(final String lemma, final Pos pos){
		assert lemma != null : "lemma is null";
		assert pos != null : "pos is null";

		PreparedStatement ps = this.findWordsByLemmaAndPos.get();
		if(ps == null){

			ps = this.createPreparedStatement(FIND_WORD_BY_LEMMA_AND_POS);
			this.findWordsByLemmaAndPos = new SoftReference<PreparedStatement>(ps);

		}

		final List<Word> ret = new ArrayList<Word>();
		try{

			ps.setString(1, lemma.toLowerCase());
			ps.setString(2, pos.toString());
			ret.addAll(this.createWords(ps));

		}catch(final SQLException e){

			e.printStackTrace();

		}

		return ret;

	}

	/**
	 * 見出し語と品詞を指定して一致するSynset集合を取得する．
	 *
	 * @param lemma 見出し語
	 * @param pos 品詞
	 * @return 見出し語lemmaと品詞posに一致するSynsetのリスト
	 */
	public List<Synset> findSynsets(final String lemma, final Pos pos){
		assert lemma != null : "lemma is null";
		assert pos != null : "pos is null";

		PreparedStatement ps = this.findSynsetsByNameAndPos.get();
		if(ps == null){

			ps = this.createPreparedStatement(FIND_SYNSETS_BY_NAME_AND_POS);
			this.findSynsetsByNameAndPos = new SoftReference<PreparedStatement>(ps);

		}

		final List<Synset> ret = new ArrayList<Synset>();
		try {

			ps.setString(1, lemma);
			ps.setString(2, pos.toString());
			ret.addAll(this.createSynsets(ps));

		}catch(final SQLException e){

			e.printStackTrace();

		}

		return ret;

	}


	/////////////////////////////////////////////////////////////////////////////////////
	// Package private methods
	/////////////////////////////////////////////////////////////////////////////////////
	Word findWord(final Word word){

		PreparedStatement ps = this.findWordByWordid.get();
		if(ps == null){

			ps = this.createPreparedStatement(FIND_WORD_BY_WORDID);
			this.findWordByWordid = new SoftReference<PreparedStatement>(ps);

		}

		final List<Word> ret = new ArrayList<Word>();
		try{

			ps.setInt(1, word.getWordID());
			ret.addAll(this.createWords(ps));

		}catch(final SQLException e){

			e.printStackTrace();

		}

		if(ret.size() == 0){

			return null;

		}

		return ret.get(0);

	}

	List<Sense> findSenses(final Word word){

		PreparedStatement ps = this.findSensesByWordid.get();
		if(ps == null){

			ps = this.createPreparedStatement(FIND_SENSES_BY_WORDID);
			this.findSensesByWordid = new SoftReference<PreparedStatement>(ps);

		}

		final List<Sense> ret = new ArrayList<Sense>();
		try{

			ps.setInt(1, word.getWordID());
			ret.addAll(this.createSences(ps));

		}catch(final SQLException e){

			e.printStackTrace();

		}

		return ret;

	}

	List<Sense> findSenses(final Synset synset){

		PreparedStatement ps = this.findSensesBySynset.get();
		if(ps == null){

			ps = this.createPreparedStatement(FIND_SENSES_BY_SYNSET);
			this.findSensesBySynset = new SoftReference<PreparedStatement>(ps);

		}

		final List<Sense> ret = new ArrayList<Sense>();
		try{

			ps.setString(1, synset.getSynsetID());
			ret.addAll(this.createSences(ps));

		}catch(final SQLException e){

			e.printStackTrace();

		}

		return ret;

	}

	List<Sense> findSenses(final Synset synset, final Lang lang){

		PreparedStatement ps = this.findSensesBySynsetAndLang.get();
		if(ps == null){

			ps = this.createPreparedStatement(FIND_SENSES_BY_SYNSET_AND_LANG);
			this.findSensesBySynsetAndLang = new SoftReference<PreparedStatement>(ps);

		}

		final List<Sense> ret = new ArrayList<Sense>();
		try{

			ps.setString(1, synset.getSynsetID());
			ps.setString(2, lang.toString());
			ret.addAll(this.createSences(ps));

		}catch(final SQLException e){

			e.printStackTrace();

		}

		return ret;

	}

	Synset findSynset(final Synset synset){

		PreparedStatement ps = this.findSynsetBySynset.get();
		if(ps == null){

			ps = this.createPreparedStatement(FIND_SYNSET_BY_SYNSET);
			this.findSynsetBySynset = new SoftReference<PreparedStatement>(ps);

		}

		final List<Synset> ret = new ArrayList<Synset>();
		try{

			ps.setString(1, synset.getSynsetID());
			ret.addAll(this.createSynsets(ps));

		}catch(final SQLException e){

			e.printStackTrace();

		}

		if(ret.size() == 0){

			return null;

		}

		return ret.get(0);

	}

	List<Synset> findSynsets(final Synset synset, final Link link){

		PreparedStatement ps = this.findSynsetBySynsetAndLink.get();
		if(ps == null){

			ps = this.createPreparedStatement(FIND_SYNSET_BY_SYNSET_AND_LINK);
			this.findSynsetBySynsetAndLink = new SoftReference<PreparedStatement>(ps);

		}

		final List<Synset> ret = new ArrayList<Synset>();
		try{

			ps.setString(1, synset.getSynsetID());
			ret.addAll(this.createSynsets(ps));

		}catch(final SQLException e){

			e.printStackTrace();

		}

		return ret;

	}


	SynsetDef findSynsetDef(final Synset synset){

		PreparedStatement ps = this.findSynsetDefBySynset.get();
		if(ps == null){

			ps = this.createPreparedStatement(FIND_SYNSETDEF_BY_SYNSET);
			this.findSynsetDefBySynset = new SoftReference<PreparedStatement>(ps);

		}

		SynsetDef ret = null;
		try{

			ps.setString(1, synset.getSynsetID());
			final ResultSet rs = ps.executeQuery();
			try{

				if(rs.next()){

					ret = new SynsetDef(this, synset, Lang.valueOf(rs.getString(2)), rs.getString(3), rs.getInt(4));

				}

			}finally{

				rs.close();

			}

		}catch(final SQLException e){

			e.printStackTrace();

		}

		return ret;

	}

	List<Synlink> findSynlinks(final Synset synset){

		PreparedStatement ps = this.findSynlinksBySynset.get();
		if(ps == null){

			ps = this.createPreparedStatement(FIND_SYNLINK_BY_SYNSET);
			this.findSynlinksBySynset = new SoftReference<PreparedStatement>(ps);

		}

		final List<Synlink> ret = new ArrayList<Synlink>();
		try{

			ps.setString(1, synset.getSynsetID());
			ret.addAll(this.createSynlinks(ps));

		}catch(final SQLException e){

			e.printStackTrace();

		}
		return ret;

	}

	List<Synlink> findSynlinks(final Synset synset, final Link link){

		PreparedStatement ps = this.findSynlinksBySynsetAndLink.get();
		if(ps == null){

			ps = this.createPreparedStatement(FIND_SYNLINK_BY_SYNSET_AND_LINK);
			this.findSynlinksBySynsetAndLink = new SoftReference<PreparedStatement>(ps);

		}

		final List<Synlink> ret = new ArrayList<Synlink>();
		try{

			ps.setString(1, synset.getSynsetID());
			ps.setString(2, link.toString());
			ret.addAll(this.createSynlinks(ps));

		}catch(final SQLException e){

			e.printStackTrace();

		}
		return ret;

	}

	/////////////////////////////////////////////////////////////////////////////////////
	// Private methods
	/////////////////////////////////////////////////////////////////////////////////////
	private List<Word> createWords(final PreparedStatement ps) throws SQLException{

		final List<Word> words = new ArrayList<Word>();
		final ResultSet rs = ps.executeQuery();
		try{

			while(rs.next()){

				Object a = rs.getObject(4);
				System.out.println(a);
				System.out.println(a.getClass());

				words.add(Word.create(this, rs.getInt(1), Lang.valueOf(rs.getString(2)), rs.getString(3), rs.getString(4), Pos.valueOf(rs.getString(5))));

			}

		}finally{

			rs.close();

		}

		return words;

	}

	private List<Sense> createSences(final PreparedStatement ps) throws SQLException{

		final List<Sense> senses = new ArrayList<Sense>();
		final ResultSet rs = ps.executeQuery();
		try{

			while(rs.next()){

				senses.add(new Sense(this, rs.getString(1), rs.getInt(2), rs.getString(3), rs.getInt(4), rs.getInt(5), rs.getInt(6), rs.getString(7)));

			}

		}finally{

			rs.close();

		}

		return senses;

	}

	private List<Synset> createSynsets(final PreparedStatement ps) throws SQLException{

		final ResultSet rs = ps.executeQuery();
		final List<Synset> synsets = new ArrayList<Synset>();
		try{

			if(rs.next()){

				synsets.add(Synset.create(this, rs.getString(1), Pos.valueOf(rs.getString(2)), rs.getString(3), rs.getString(4)));

			}

		}finally{

			rs.close();

		}

		return synsets;

	}

	private List<Synlink> createSynlinks(final PreparedStatement ps) throws SQLException{

		final List<Synlink> synlinks = new ArrayList<Synlink>();
		final ResultSet rs = ps.executeQuery();
		try{

			while(rs.next()){

				synlinks.add(new Synlink(this, rs.getString(1), rs.getString(2), Link.valueOf(rs.getString(3)), rs.getString(4)));

			}

		}finally{

			rs.close();

		}

		return synlinks;

	}

	private PreparedStatement createPreparedStatement(final String sql){

		PreparedStatement ret = null;
		try{

			ret = this.connection.prepareStatement(sql);

		}catch(final SQLException e){

			e.printStackTrace();

		}

		assert ret != null;
		return ret;

	}

}
