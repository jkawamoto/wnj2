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

package org.wnj2.converter;

import java.io.Console;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Converter {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		if(args.length < 3){
			System.out.println("Useage: <Wordnet File's Path> <MySQL DB's URL> <Username of MySQL DB>");
			System.exit(1);
		}
		try {

			Class.forName("org.sqlite.JDBC");
			Class.forName("org.gjt.mm.mysql.Driver");

		} catch (ClassNotFoundException e) {

			e.printStackTrace();
			System.exit(1);

		}

		final String srcUri = String.format("jdbc:sqlite:%s", args[0]);
		final String destUri = args[1];
		final String destUser =args[2];

		final Console console = System.console();
		final String password = new String(console.readPassword("Password: "));

		try {

			final Connection srcCon = DriverManager.getConnection(srcUri);
			final Statement srcStat = srcCon.createStatement();

			final Connection destCon = DriverManager.getConnection(destUri, destUser, password);
			final Statement destStat = destCon.createStatement();


			System.out.println("Copying LINK_DEF table");
			try{

				// 既にテーブルがある場合は削除
				destStat.executeUpdate("drop table if exists LINK_DEF;");

				// 新たにテーブルを作成
				destStat.executeUpdate("create table LINK_DEF(link varchar(5) primary key, lang char(3) not null, def text not null);");

				// 元のデータベースからコピー
				final ResultSet rs = srcStat.executeQuery("select * from LINK_DEF");
				try{

					while(rs.next()){

						// read the result set
						final String link = rs.getString("link");
						final String lang = rs.getString("lang");
						final String def = rs.getString("def");

						if(link != null && lang != null && def != null){

							destStat.executeUpdate(String.format("insert into LINK_DEF values('%s', '%s', '%s');", link, lang, def));

						}

					}

				}finally{

					rs.close();

				}

			}catch(final SQLException e){

				e.printStackTrace();
				System.exit(1);

			}


			System.out.println("Copying POS_DEF table");
			try{

				// 既にテーブルがある場合は削除
				destStat.executeUpdate("drop table if exists POS_DEF;");

				// 新たにテーブルを作成
				destStat.executeUpdate("create table POS_DEF(pos char(1), lang char(3), def text not null, primary key(pos, lang));");

				// 元のデータベースからコピー
				final ResultSet rs = srcStat.executeQuery("select * from POS_DEF");
				try{

					while(rs.next()){

						// read the result set
						final String pos = rs.getString("pos");
						final String lang = rs.getString("lang");
						final String def = rs.getString("def");

						if(pos != null && lang != null && def != null){

							destStat.executeUpdate(String.format("insert into POS_DEF values('%s', '%s', '%s');", pos, lang, def));

						}

					}

				}finally{

					rs.close();

				}

			}catch(final SQLException e){

				e.printStackTrace();
				System.exit(1);

			}

			System.out.println("Copying SENSE table");
			try{

				// 既にテーブルがある場合は削除
				destStat.executeUpdate(String.format("drop table if exists SENSE;"));

				// 新たにテーブルを作成
				destStat.executeUpdate("create table SENSE(synset varchar(12), wordid integer unsigned, lang char(3), rank integer unsigned, lexid integer unsigned, freq integer unsigned, src varchar(10), primary key(synset, wordid));");

				// 元のデータベースからコピー
				final ResultSet rs = srcStat.executeQuery("select * from SENSE");
				try{

					while(rs.next()){

						// read the result set
						final String synset = rs.getString("synset");
						final int wordid = rs.getInt("wordid");
						final String lang = rs.getString("lang");
						final int rank = rs.getInt("rank");
						final int lexid = rs.getInt("lexid");
						final int freq = rs.getInt("freq");
						final String src = rs.getString("src");

						destStat.executeUpdate(String.format("insert into sense values('%s', %d, '%s', %d, %d, %d, '%s');", synset, wordid, lang, rank, lexid, freq, src));

					}

				}finally{

					rs.close();

				}

			}catch(final SQLException e){

				e.printStackTrace();
				System.exit(1);

			}

			System.out.println("Copying SYNLINK table");
			try{

				// 既にテーブルがある場合は削除
				destStat.executeUpdate(String.format("drop table if exists SYNLINK;"));

				// 新たにテーブルを作成
				destStat.executeUpdate("create table SYNLINK(synset1 varchar(12), synset2 varchar(12), link varchar(5), src varchar(10), primary key(synset1, synset2, link));");

				// 元のデータベースからコピー
				final ResultSet rs = srcStat.executeQuery("select * from SYNLINK");
				try{
					while(rs.next()){

						// read the result set
						final String synset1 = rs.getString("synset1");
						final String synset2 = rs.getString("synset2");
						final String link = rs.getString("link");
						final String src = rs.getString("src");

						destStat.executeUpdate(String.format("INSERT INTO SYNLINK VALUES('%s', '%s', '%s', '%s');", synset1, synset2, link, src));

					}

				}finally{

					rs.close();

				}

			}catch(final SQLException e){

				e.printStackTrace();
				System.exit(1);

			}

			System.out.println("Copying SYNSET table");
			try{

				// 既にテーブルがある場合は削除
				destStat.executeUpdate(String.format("drop table if exists SYNSET;"));

				// 新たにテーブルを作成
				destStat.executeUpdate("create table SYNSET(synset varchar(12) primary key, pos char(1), name text, src varchar(10));");

				// 元のデータベースからコピー
				final ResultSet rs = srcStat.executeQuery("select * from SYNSET");
				try{

					while(rs.next()){

						// read the result set
						final String synset = rs.getString("synset");
						final String pos = rs.getString("pos");
						final String name = rs.getString("name");
						final String src = rs.getString("src");

						destStat.executeUpdate(String.format("INSERT INTO SYNSET VALUES('%s', '%s', \"%s\", '%s');", synset, pos, name, src));

					}

				}finally{

					rs.close();

				}

			}catch(final SQLException e){

				e.printStackTrace();
				System.exit(1);

			}

			System.out.println("Copying SYNSET_DEF table");
			try{

				// 既にテーブルがある場合は削除
				destStat.executeUpdate(String.format("drop table if exists SYNSET_DEF;"));

				// 新たにテーブルを作成
				destStat.executeUpdate("create table SYNSET_DEF(synset varchar(12) primary key, lang char(3), def text, sid integer unsigned);");

				// 元のデータベースからコピー
				final ResultSet rs = srcStat.executeQuery("select * from SYNSET_DEF");
				try{

					while(rs.next()){

						// read the result set
						final String synset = rs.getString("synset");
						final String lang = rs.getString("lang");
						final String def = rs.getString("def");
						final String sid = rs.getString("sid");

						final String sql = String.format("INSERT INTO SYNSET_DEF VALUES('%s', '%s', \"%s\", '%s');", synset, lang, def.replace("\"", "\\\""), sid);
						destStat.executeUpdate(sql);

					}

				}finally{

					rs.close();

				}

			}catch(final SQLException e){

				e.printStackTrace();
				System.exit(1);

			}

			System.out.println("Copying WORD table");
			try{

				// 既にテーブルがある場合は削除
				destStat.executeUpdate(String.format("drop table if exists WORD;"));

				// 新たにテーブルを作成
				destStat.executeUpdate("create table WORD(wordid integer primary key, lang char(3), lemma text, pron text, pos char(1));");

				// 元のデータベースからコピー
				final ResultSet rs = srcStat.executeQuery("select * from WORD");
				try{

					while(rs.next()){

						// read the result set
						final int wordid = rs.getInt("wordid");
						final String lang = rs.getString("lang");
						final String lemma = rs.getString("lemma");
						final String pron = rs.getString("pron");
						final String pos = rs.getString("pos");

						destStat.executeUpdate(String.format("INSERT INTO WORD VALUES(%d, '%s', \"%s\", \"%s\", '%s');", wordid, lang, lemma.replace("\"", "\\\""), pron, pos));

					}

				}finally{

					rs.close();

				}

			}catch(final SQLException e){

				e.printStackTrace();
				System.exit(1);

			}

			destStat.close();
			destCon.close();
			srcStat.close();
			srcCon.close();

		} catch (SQLException e) {

			e.printStackTrace();

		}

	}

}

