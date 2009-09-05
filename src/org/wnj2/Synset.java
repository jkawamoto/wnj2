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


public abstract class Synset {

	protected final Wnj2 parent;
	private final String synsetid;

	/////////////////////////////////////////////////////////////////////////////////////
	// Constractor
	/////////////////////////////////////////////////////////////////////////////////////
	protected Synset(final Wnj2 parent, final String id){

		this.parent = parent;
		this.synsetid = id;

	}

	/////////////////////////////////////////////////////////////////////////////////////
	// Public methods
	/////////////////////////////////////////////////////////////////////////////////////
	public String getSynsetID() {

		return this.synsetid;

	}

	public abstract Pos getPos();
	public abstract String getName();
	public abstract String getSrc();

	public SynsetDef getDef(){

		return this.parent.findSynsetDef(this);

	}

	public List<Sense> getSenses(){

		return this.parent.findSenses(this);

	}

	public List<Sense> getSenses(final Lang lang){

		return this.parent.findSenses(this, lang);

	}

	public List<Synlink> getSynlinks(){

		return this.parent.findSynlinks(this);

	}

	public List<Synlink> getSynlinks(final Link link){

		return this.parent.findSynlinks(this, link);

	}

	/* (非 Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {

		try{

			final Synset that = (Synset)obj;
			return this.getSynsetID().equals(that.getSynsetID());

		}catch(final ClassCastException e){

			return super.equals(obj);

		}

	}

	/* (非 Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {

		return this.getSynsetID().hashCode();

	}

	/* (非 Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {

		return String.format("Synset[ synsetid: %s, pos: %s, name: %s, src: %s]", this.getSynsetID(), this.getPos(), this.getName(), this.getSrc());

	}

	/////////////////////////////////////////////////////////////////////////////////////
	// Static methods
	/////////////////////////////////////////////////////////////////////////////////////
	static Synset create(final Wnj2 parent, final String id){

		return new LazySynset(parent, id);

	}

	static Synset create(final Wnj2 parent, final String id, final Pos pos, final String name, final String src){

		return new InitializedSynset(parent, id, pos, name, src);

	}


}
