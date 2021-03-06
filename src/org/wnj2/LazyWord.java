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

class LazyWord extends Word{

	private Word impl = null;

	/////////////////////////////////////////////////////////////////////////////////////
	// Constractor
	/////////////////////////////////////////////////////////////////////////////////////
	LazyWord(final Wnj2 parent, final int wordid){
		super(parent, wordid);

	}

	/////////////////////////////////////////////////////////////////////////////////////
	// Public methods
	/////////////////////////////////////////////////////////////////////////////////////
	/* (非 Javadoc)
	 * @see org.wnj2.Word#getLang()
	 */
	@Override
	public Lang getLang() {

		return this.getImpl().getLang();

	}

	/* (非 Javadoc)
	 * @see org.wnj2.Word#getLemma()
	 */
	@Override
	public String getLemma() {

		return this.getImpl().getLemma();

	}

	/* (非 Javadoc)
	 * @see org.wnj2.Word#getPos()
	 */
	@Override
	public Pos getPos() {

		return this.getImpl().getPos();

	}

	/* (非 Javadoc)
	 * @see org.wnj2.Word#getPron()
	 */
	@Override
	public String getPron() {

		return this.getImpl().getPron();

	}

	/////////////////////////////////////////////////////////////////////////////////////
	// Protected methods
	/////////////////////////////////////////////////////////////////////////////////////
	protected Word getImpl(){

		if(this.impl == null){

			try {

				this.impl = this.parent.findWord(this);

			}catch(final SQLException e){

				this.impl = new DummyWord(this.parent, -1);

			}

		}

		return this.impl;

	}

	private class DummyWord extends Word{

		protected DummyWord(Wnj2 parent, int wordid) {
			super(parent, wordid);
		}

		@Override
		public Lang getLang() {
			return null;
		}

		@Override
		public String getLemma() {
			return "";
		}

		@Override
		public Pos getPos() {
			return null;
		}

		@Override
		public String getPron() {
			return "";
		}

	};

}
