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

public class Synlink {

	protected final Wnj2 parent;

	private final Synset from;
	private final Synset to;
	private final Link link;
	private final String src;

	/////////////////////////////////////////////////////////////////////////////////////
	// Constractor
	/////////////////////////////////////////////////////////////////////////////////////
	Synlink(final Wnj2 parent, final String from, final String to, final Link link, final String src){

		this.parent = parent;
		this.from = Synset.create(parent, from);
		this.to = Synset.create(parent, to);
		this.link = link;
		this.src = src;

	}

	/////////////////////////////////////////////////////////////////////////////////////
	// Public methods
	/////////////////////////////////////////////////////////////////////////////////////
	public Synset getFrom() {
		return this.from;
	}

	public Synset getTo() {
		return this.to;
	}

	public Link getLink() {
		return this.link;
	}

	public String getSrc() {
		return this.src;
	}

	/* (非 Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {

		if(obj instanceof Synlink){

			final Synlink that = (Synlink)obj;
			return this.getLink().equals(that.getLink()) && this.getFrom().equals(that.getFrom()) && this.getTo().equals(that.getTo());

		}

		return super.equals(obj);

	}

	/* (非 Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {

		return this.getLink().hashCode() * this.getFrom().hashCode() * this.getTo().hashCode();

	}

	/* (非 Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {

		return String.format("Synlink[ from: %s, to: %s, link: %s, src: %s]", this.getFrom().getSynsetID(), this.getTo().getSynsetID(), this.getLink(), this.getSrc());

	}

}
