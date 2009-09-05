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
	public int getWordID(){

		return this.wordid;

	}

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



	@Override
	public boolean equals(final Object obj){

		try{

			final Word that = (Word)obj;
			return this.getWordID() == that.getWordID();

		}catch(final ClassCastException e){

			return false;

		}

	}

	@Override
	public int hashCode() {

		return Integer.valueOf(this.getWordID()).hashCode();

	}

	@Override
	public String toString() {
		// TODO 自動生成されたメソッド・スタブ
		return super.toString();
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
