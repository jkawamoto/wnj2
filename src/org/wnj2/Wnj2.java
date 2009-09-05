package org.wnj2;

import java.io.Closeable;
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

public class Wnj2 implements Closeable{

	private final Connection _connection;

	private static final String FIND_WORD_BY_LEMMA = "select * from WORD where lemma = ?;";
	private static final String FIND_WORD_BY_LEMMA_AND_POS = "select * from WORD where lemma = ? and pos = ?;";
	private static final String FIND_WORD_BY_WORDID = "select * from WORD where wordid = ?;";

	private static final String FIND_SENSES_BY_WORDID = "select * from SENSE where wordid = ?";
	private static final String FIND_SENSES_BY_SYNSET = "select * from SENSE where synset = ?";
	private static final String FIND_SENSES_BY_SYNSET_AND_LANG = "select * from SENSE where synset = ? and lang = ?";

	private static final String FIND_SYNSETS_BY_NAME_AND_POS = "select * from SYNSET where name = ? and pos = ?;";
	private static final String FIND_SYNSET_BY_SYNSET = "select * from SYNSET where synset = ?;";

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

	private Reference<PreparedStatement> findSynsetDefBySynset = new SoftReference<PreparedStatement>(null);

	private Reference<PreparedStatement> findSynlinksBySynset = new SoftReference<PreparedStatement>(null);
	private Reference<PreparedStatement> findSynlinksBySynsetAndLink = new SoftReference<PreparedStatement>(null);

	/////////////////////////////////////////////////////////////////////////////////////
	// Constractors
	/////////////////////////////////////////////////////////////////////////////////////
	public Wnj2() throws ClassNotFoundException, SQLException{

		Class.forName("org.sqlite.JDBC");
		this._connection = DriverManager.getConnection("jdbc:sqlite:./data/wnjpn-0.9.db");

	}

	public Wnj2(final Connection connection){

		this._connection = connection;

	}

	/////////////////////////////////////////////////////////////////////////////////////
	// Public methods
	/////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void close() throws IOException {

		try {

			this._connection.close();

		} catch (SQLException e) {

			throw new IOException(e.getMessage());

		}

	}

	public List<Word> findWords(final String lemma){

		PreparedStatement ps = this.findWordsByLemma.get();
		if(ps == null){

			ps = this.createPreparedStatement(FIND_WORD_BY_LEMMA);
			this.findWordsByLemma = new SoftReference<PreparedStatement>(ps);

		}

		final List<Word> ret = new ArrayList<Word>();
		try {

			ps.setString(1, lemma.toLowerCase());
			ret.addAll(this.createWords(ps));

		} catch ( SQLException e ) {
			e.printStackTrace();
		}

		return ret;

	}

	public List<Word> findWords(final String lemma, final Pos pos){

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

		}catch(SQLException e){
			e.printStackTrace();
		}

		return ret;

	}

	public List<Synset> findSynsets(final String name, final Pos pos){

		PreparedStatement ps = this.findSynsetsByNameAndPos.get();
		if(ps == null){

			ps = this.createPreparedStatement(FIND_SYNSETS_BY_NAME_AND_POS);
			this.findSynsetsByNameAndPos = new SoftReference<PreparedStatement>(ps);

		}

		final List<Synset> ret = new ArrayList<Synset>();
		try {

			ps.setString(1, name);
			ps.setString(2, pos.toString());
			ret.addAll(this.createSynsets(ps));

		} catch ( SQLException e ) {
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
		try {

			ps.setInt(1, word.getWordID());
			ret.addAll(this.createWords(ps));

		} catch ( SQLException e ) {
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
		try {

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
		try {

			ps.setString(1, synset.getSynsetID());
			ret.addAll(this.createSynsets(ps));

		} catch ( SQLException e ) {
			e.printStackTrace();
		}

		if(ret.size() == 0){

			return null;

		}

		return ret.get(0);

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

				words.add(Word.create(this, rs.getInt(1), Lang.valueOf(rs.getString(2)), rs.getString(3), rs.getString(4), rs.getString(5)));

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

		try {

			return this._connection.prepareStatement(sql);

		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

		return null;

	}


}
