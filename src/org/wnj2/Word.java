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

import java.sql.SQLException;
import java.util.List;

/**
 * Wordタプルのラッパオブジェクト．
 *
 * <p>
 * 日本語WordNetでは，Wordテーブルは次のように定義されています．
 * </p>
 * <blockquote><pre>
 * CREATE TABLE WORD(
 *     wordid                      INTEGER(2000000000,10),
 *     lang                        TEXT,
 *     lemma                       TEXT,
 *     pron                        TEXT,
 *     pos                         TEXT,
 *     CONSTRAINT null PRIMARY KEY (wordid),
 *     CONSTRAINT word_id_idx UNIQUE (wordid),
 *     CONSTRAINT word_word_idx UNIQUE (lemma)
 * )
 * </pre></blockquote>
 * <p>
 * Wordオブジェクトは，このテーブルのタプル一つ一つに対応し，各属性へのアクセッサを提供します．
 * </p>
 * <p>
 * Wordオブジェクトを取得するには，見出し語または見出し語と品詞を指定してWnj2オブジェクトのfindWordsメソッドを呼びます．
 * また，Wordオブジェクトから関係するSenseやSynsetを取得することが出来ます．
 * </p>
 *
 * @see Sense
 * @see Synset
 *
 */
public abstract class Word {

	protected final Wnj2 parent;
	private final int wordid;

	/////////////////////////////////////////////////////////////////////////////////////
	// Constractor
	/////////////////////////////////////////////////////////////////////////////////////
	protected Word(final Wnj2 parent, final int wordid){

		this.parent = parent;
		this.wordid = wordid;

	}

	/////////////////////////////////////////////////////////////////////////////////////
	// Public methods
	/////////////////////////////////////////////////////////////////////////////////////
	/**
	 * IDを取得する．
	 *
	 * @return このWordを表すID
	 */
	public int getWordID(){

		return this.wordid;

	}

	/**
	 * 言語を取得する．
	 *
	 * @return このWordの言語
	 */
	public abstract Lang getLang();

	/**
	 * 見出し語を取得する．
	 *
	 * @return このWordの見出し語
	 */
	public abstract String getLemma();

	public abstract String getPron();

	/**
	 * 品詞を取得する．
	 *
	 * @return このWordの品詞
	 */
	public abstract Pos getPos();

	/**
	 * Sense集合を取得する．
	 *
	 * @return このWordに関係するSenseのリスト
	 * @throws SQLException データベースへのアクセスにエラーが発生した場合
	 */
	public List<Sense> getSenses() throws SQLException{

		return this.parent.findSenses(this);

	}

	/**
	 * Synset集合を取得する．
	 *
	 * @return このWordに関係するSynsetのリスト
	 * @throws SQLException データベースへのアクセスにエラーが発生した場合
	 */
	public List<Synset> getSynsets() throws SQLException{

		return this.parent.findSynsets(this.getLemma(), this.getPos());

	}

	/**
	 * このオブジェクトと指定されたオブジェクトを比較します．
	 * このメソッドは，WordIDが等しい場合にtrueを返します．
	 *
	 * @param obj このオブジェクトと比較されるオブジェクト
	 * @return 比較対象のオブジェクトとWordIDが等しい場合はtrue, そうでない場合はfalse
	 */
	@Override
	public boolean equals(final Object obj){

		try{

			final Word that = (Word)obj;
			return this.getWordID() == that.getWordID();

		}catch(final ClassCastException e){

			return false;

		}

	}

	/**
	 * このオブジェクトのハッシュ値を取得する．
	 * ハッシュ値は，WordIDのみから計算されます．
	 *
	 * @return このオブジェクトのハッシュ値
	 */
	@Override
	public int hashCode() {

		return Integer.valueOf(this.getWordID()).hashCode();

	}

	/**
	 * このオブジェクトのJSON表記を取得する．
	 *
	 * @return このオブジェクトをJSON形式で表した文字列．
	 */
	@Override
	public String toString() {

		return String.format("{\"wordid\": %d, \"lang\": \"%s\", \"lemma\": \"%s\", \"pron\": \"%s\", \"pos\": \"%s\"}",
				this.getWordID(),
				this.getLang(),
				this.getLemma().replace("\"", "\\\""),
				this.getPron().replace("\"", "\\\""),
				this.getPos());

	}

	/////////////////////////////////////////////////////////////////////////////////////
	// Static methods
	/////////////////////////////////////////////////////////////////////////////////////
	static Word create(final Wnj2 parent, final int id){

		return new LazyWord(parent, id);

	}

	static Word create(final Wnj2 parent, final int id, final Lang lang, final String lemma, final String pron, final Pos pos){

		return new InitializedWord(parent, id, lang, lemma, pron, pos);

	}

}
