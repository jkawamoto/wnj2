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

class InitializedWord extends Word{

	private final Lang lang;
	private final String lemma;
	private final String pron;
	private final Pos pos;

	/////////////////////////////////////////////////////////////////////////////////////
	// Constractor
	/////////////////////////////////////////////////////////////////////////////////////
	InitializedWord(final Wnj2 parent, final int id, final Lang lang, final String lemma, final String pron, final String pos){
		super(parent, id);

		this.lang = lang;
		this.lemma = lemma;
		this.pron = pron;
		this.pos = Pos.valueOf(pos);

	}

	/////////////////////////////////////////////////////////////////////////////////////
	// Public methods
	/////////////////////////////////////////////////////////////////////////////////////
	/* (非 Javadoc)
	 * @see org.wnj2.Word#getLang()
	 */
	@Override
	public Lang getLang() {
		return this.lang;
	}

	/* (非 Javadoc)
	 * @see org.wnj2.Word#getLemma()
	 */
	@Override
	public String getLemma() {
		return this.lemma;
	}

	/* (非 Javadoc)
	 * @see org.wnj2.Word#getPron()
	 */
	@Override
	public String getPron() {
		return this.pron;
	}

	/* (非 Javadoc)
	 * @see org.wnj2.Word#getPos()
	 */
	@Override
	public Pos getPos() {
		return this.pos;
	}

}
