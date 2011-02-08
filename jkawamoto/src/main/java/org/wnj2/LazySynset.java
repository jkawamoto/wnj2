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

class LazySynset extends Synset{

	private Synset impl = null;

	/////////////////////////////////////////////////////////////////////////////////////
	// Constractor
	/////////////////////////////////////////////////////////////////////////////////////
	LazySynset(final Wnj2 parent, final String id){
		super(parent, id);

	}

	/////////////////////////////////////////////////////////////////////////////////////
	// Public methods
	/////////////////////////////////////////////////////////////////////////////////////
	/* (非 Javadoc)
	 * @see org.wnj2.Synset#getName()
	 */
	@Override
	public String getName() {

		return this.getImpl().getName();

	}

	/* (非 Javadoc)
	 * @see org.wnj2.Synset#getPos()
	 */
	@Override
	public Pos getPos() {

		return this.getImpl().getPos();

	}

	/* (非 Javadoc)
	 * @see org.wnj2.Synset#getSrc()
	 */
	@Override
	public String getSrc() {

		return this.getImpl().getSrc();

	}

	/////////////////////////////////////////////////////////////////////////////////////
	// Protected methods
	/////////////////////////////////////////////////////////////////////////////////////
	protected Synset getImpl(){

		if(this.impl == null){

			try {

				this.impl = this.parent.findSynset(this);

			}catch(final SQLException e){

				this.impl = new DummySynset(this.parent, "");

			}

		}

		return this.impl;

	}


	private class DummySynset extends Synset{

		protected DummySynset(Wnj2 parent, String id) {
			super(parent, id);
		}

		@Override
		public String getName() {
			return "";
		}

		@Override
		public Pos getPos() {
			return null;
		}

		@Override
		public String getSrc() {
			return "";
		}

	}

}
