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
 * Synlinkタプルのラッパオブジェクト．
 *
 * <p>
 * 日本語WordNetでは，Synlinkテーブルは次のように定義されています．
 * </p>
 *
 * <blockquote><pre>
 * CREATE TABLE SYNLINK(
 *     synset1                     TEXT,
 *     synset2                     TEXT,
 *     link                        TEXT,
 *     src                         TEXT,
 *     CONSTRAINT synlink_idx UNIQUE (synset1, link)
 * )
 * </pre></blockquote>
 * <p>
 * Synlinkオブジェクトは，このテーブルのタプル一つ一つに対応し，各属性へのアクセッサを提供します．
 * </p>
 *
 * @see Synset
 */
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
	/**
	 * リンク元を取得する．
	 *
	 * @return リンク元のSynset
	 */
	public Synset getFrom() {
		return this.from;
	}

	/**
	 * リンク先を取得する．
	 *
	 * @return リンク先のSynset
	 */
	public Synset getTo() {
		return this.to;
	}

	/**
	 * リンクの種類を取得する．
	 *
	 * @return リンクの種類
	 */
	public Link getLink() {
		return this.link;
	}

	public String getSrc() {
		return this.src;
	}

	/**
	 * このオブジェクトと指定されたオブジェクトを比較します．
	 * このメソッドは，getLink, getFrom, getToのすべてが等しい場合にtrueを返します．
	 *
	 * @param obj このオブジェクトと比較されるオブジェクト
	 * @return 比較対象のオブジェクトとgetLink, getFrom, getToがすべて等しい場合はtrue, そうでない場合はfalse
	 */
	@Override
	public boolean equals(final Object obj) {

		if(obj instanceof Synlink){

			final Synlink that = (Synlink)obj;
			return this.getLink().equals(that.getLink()) && this.getFrom().equals(that.getFrom()) && this.getTo().equals(that.getTo());

		}

		return super.equals(obj);

	}

	/**
	 * このオブジェクトのハッシュ値を取得する．
	 * ハッシュ値は，以下の式で計算されます．
	 * <blockquote><pre>
	 * (this.getLink().toString() + this.getFrom().toString() + this.getTo().toString()).hashCode()
	 * </pre></blockquote>
	 *
	 * @return このオブジェクトのハッシュ値
	 */
	@Override
	public int hashCode() {

		return (this.getLink().toString() + this.getFrom().toString() + this.getTo().toString()).hashCode();

	}

	/**
	 * このオブジェクトのJSON表記を取得する．
	 *
	 * @return このオブジェクトをJSON形式で表した文字列．
	 */
	@Override
	public String toString() {

		return String.format("{\"from\": \"%s\", \"to\": \"%s\", \"link\": \"%s\", \"src\": \"%s\"}",
				this.getFrom().getSynsetID(),
				this.getTo().getSynsetID(),
				this.getLink(),
				this.getSrc().replace("\"", "\\\""));

	}

}
