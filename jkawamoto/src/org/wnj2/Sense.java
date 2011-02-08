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

/**
 * Senseタプルのラッパオブジェクト．
 *
 * <p>
 * 日本語WordNetでは，Senseテーブルは次のように定義されています．
 * </p>
 * <blockquote><pre>
 * CREATE TABLE SENSE(
 *     synset                      TEXT,
 *     wordid                      INTEGER(2000000000,10),
 *     lang                        TEXT,
 *     rank                        TEXT,
 *     lexid                       INTEGER(2000000000,10),
 *     freq                        INTEGER(2000000000,10),
 *     src                         TEXT,
 *     CONSTRAINT sense_idx UNIQUE (synset, wordid)
 * )
 * </pre></blockquote>
 * <p>
 * Senseオブジェクトは，このテーブルのタプル一つ一つに対応し，各属性へのアクセッサを提供します．
 * </p>
 *
 * @see Word
 */
public class Sense {

	protected final Wnj2 parent;

	private final Synset synset;
	private final Word word;
	private final String lang;
	private final int rank;
	private final int lexid;
	private final int freq;
	private final String src;

	/////////////////////////////////////////////////////////////////////////////////////
	// Constractor
	/////////////////////////////////////////////////////////////////////////////////////
	Sense(final Wnj2 parent, final String synset, final int word, final String lang, final int rank, final int lex, final int freq, final String src){

		this.parent = parent;
		this.synset = Synset.create(parent, synset);
		this.word = Word.create(parent, word);
		this.lang = lang;
		this.rank = rank;
		this.lexid = lex;
		this.freq = freq;
		this.src = src;

	}

	/////////////////////////////////////////////////////////////////////////////////////
	// Public methods
	/////////////////////////////////////////////////////////////////////////////////////
	/**
	 * このオブジェクトと関連するSynsetを取得する．
	 *
	 * @return このオブジェクトと関連するSynset
	 */
	public Synset getSynset() {
		return this.synset;
	}

	/**
	 * このオブジェクトと関連するWordを取得する．
	 * @return このオブジェクトと関連するWord
	 */
	public Word getWord(){
		return this.word;
	}

	/**
	 * このオブジェクトの言語を取得する．
	 * @return 言語
	 */
	public String getLang() {
		return this.lang;
	}

	public int getRank() {
		return this.rank;
	}

	public int getLexID() {
		return this.lexid;
	}

	public int getFreq() {
		return this.freq;
	}

	public String getSrc() {
		return this.src;
	}

	/**
	 * このオブジェクトと指定されたオブジェクトを比較します．
	 * このメソッドは，getSynsetの値とgetWordの値が共に等しい場合にtrueを返します．
	 *
	 * @param obj このオブジェクトと比較されるオブジェクト
	 * @return 比較対象のオブジェクトとgetSynsetの値とgetWordの値が共に等しい場合はtrue, そうでない場合はfalse
	 */
	@Override
	public boolean equals(final Object obj){

		if(obj instanceof Sense){

			final Sense that = (Sense)obj;
			return this.getSynset().equals(that.getSynset()) && this.getWord().equals(that.getWord());

		}

		return super.equals(obj);

	}

	/**
	 * このオブジェクトのハッシュ値を取得する．
	 * ハッシュ値は，以下の式で計算されます．
	 * <blockquote><pre>
	 * (this.getSynset().getSynsetID() + this.getWord().getWordID()).hashCode()
	 * </pre></blockquote>
	 *
	 * @return このオブジェクトのハッシュ値
	 */
	@Override
	public int hashCode(){

		return (this.getSynset().getSynsetID() + this.getWord().getWordID()).hashCode();

	}

	/**
	 * このオブジェクトのJSON表記を取得する．
	 *
	 * @return このオブジェクトをJSON形式で表した文字列．
	 */
	@Override
	public String toString() {

		return String.format("{\"synsetid\": \"%s\", \"wordid\": %d, \"lang\": \"%s\", \"rank\": %d, \"lexid\": %d, \"freq\": %d, \"src\": \"%s\"}",
				this.getSynset().getSynsetID(),
				this.getWord().getWordID(),
				this.getLang(),
				this.getRank(),
				this.getLexID(),
				this.getFreq(),
				this.getSrc().replace("\"", "\\\""));

	}

}
