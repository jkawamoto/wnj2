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

class InitializedSynset extends Synset{

	private final Pos pos;
	private final String name;
	private final String src;

	InitializedSynset(final Wnj2 parent, final String id, final Pos pos, final String name, final String src){
		super(parent, id);

		this.pos = pos;
		this.name = name;
		this.src = src;

	}

	/* (非 Javadoc)
	 * @see org.wnj2.Synset#getPos()
	 */
	@Override
	public Pos getPos() {
		return this.pos;
	}

	/* (非 Javadoc)
	 * @see org.wnj2.Synset#getName()
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/* (非 Javadoc)
	 * @see org.wnj2.Synset#getSrc()
	 */
	@Override
	public String getSrc() {
		return this.src;
	}

}
