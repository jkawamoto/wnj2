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

import java.util.List;


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
	 * WordIDを取得する．
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
	public abstract String getLemma();
	public abstract String getPron();
	public abstract Pos getPos();

	public List<Sense> getSenses(){

		return this.parent.findSenses(this);

	}

	public List<Synset> getSynsets(){

		return this.parent.findSynsets(this.getLemma(), this.getPos());

	}

	/* (非 Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
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

	/* (非 Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {

		return Integer.valueOf(this.getWordID()).hashCode();

	}

	/* (非 Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {

		return String.format("Word[ wordid: %d, lang: %s, lemma: %s, pron: %s, pos: %s]", this.getWordID(), this.getLang(), this.getLemma(), this.getPron(), this.getPos());

	}

	/////////////////////////////////////////////////////////////////////////////////////
	// Static methods
	/////////////////////////////////////////////////////////////////////////////////////
	static Word create(final Wnj2 parent, final int id){

		return new LazyWord(parent, id);

	}

	static Word create(final Wnj2 parent, final int id, final Lang lang, final String lemma, final String pron, final String pos){

		return new InitializedWord(parent, id, lang, lemma, pron, pos);

	}

}
