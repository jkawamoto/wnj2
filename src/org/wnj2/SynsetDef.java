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
 * SynsetDefタプルのラッパオブジェクト．
 *
 * <p>
 * 日本語WordNetでは，SynsetDefテーブルは次のように定義されており，
 * 個々のSynsetに対する説明文を格納しています．
 * </p>
 * <blockquote><pre>
 * CREATE TABLE SYNSET_DEF(
 *     synset                      TEXT,
 *     lang                        TEXT,
 *     def                         TEXT,
 *     sid                         TEXT
 * )
 * </pre></blockquote>
 *
 * @see Synset
 *
 */
public class SynsetDef {

	protected final Wnj2 parent;
	private final Synset synset;
	private final Lang lang;
	private final String def;
	private final int sid;

	/////////////////////////////////////////////////////////////////////////////////////
	// Constractor
	/////////////////////////////////////////////////////////////////////////////////////
	SynsetDef(final Wnj2 parent, final Synset synset, final Lang lang, final String def, final int sid){

		this.parent = parent;
		this.synset = synset;
		this.lang = lang;
		this.def = def;
		this.sid = sid;

	}

	/////////////////////////////////////////////////////////////////////////////////////
	// Public methods
	/////////////////////////////////////////////////////////////////////////////////////
	public Synset getSynset() {
		return this.synset;
	}

	/**
	 * 言語を取得する．
	 *
	 * @return このSynset定義の言語
	 */
	public Lang getLang() {
		return this.lang;
	}

	/**
	 * Synsetの定義を取得する．
	 *
	 * @return 定義
	 */
	public String getDef() {
		return this.def;
	}

	public int getSID() {
		return this.sid;
	}

	/* (非 Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {

		if(obj instanceof SynsetDef){

			final SynsetDef that = (SynsetDef)obj;
			return this.getSynset().equals(that.getSynset());

		}

		return super.equals(obj);
	}

	/* (非 Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {

		return this.getSynset().hashCode();

	}

	/* (非 Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {

		return String.format("{\"synsetid\": \"%s\", \"lang\": \"%s\", \"def\": \"%s\", \"sid\": %d}",
				this.getSynset(),
				this.getLang(),
				this.getDef().replace("\"", "\\\""),
				this.getSID());

	}

}
