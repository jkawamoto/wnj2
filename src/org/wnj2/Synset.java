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
import java.util.List;

/**
 * Synsetタプルのラッパオブジェクト．
 *
 * <p>
 * 日本語WordNetでは，Synsetテーブルは次のように定義されています．
 * </p>
 * <blockquote><pre>
 * CREATE TABLE SYNSET(
 *     synset                      TEXT,
 *     pos                         TEXT,
 *     name                        TEXT,
 *     src                         TEXT
 * )
 * </pre></blockquote>
 * <p>
 * Synsetオブジェクトは，このテーブルのタプル一つ一つに対応し，各属性へのアクセッサを提供します．
 * </p>
 *
 * @see SynsetDef
 * @see Sense
 * @see Synlink
 */
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
	/**
	 * SynsetIDを取得する．
	 *
	 * @return このオブジェクトのSynsetID
	 */
	public String getSynsetID() {

		return this.synsetid;

	}

	/**
	 * このオブジェクトの品詞を取得する．
	 * @return このオブジェクトの品詞
	 */
	public abstract Pos getPos();

	/**
	 * このオブジェクトの名前を取得する．
	 *
	 * @return このオブジェクトの名前
	 */
	public abstract String getName();


	public abstract String getSrc();

	/**
	 * このオブジェクトの定義を取得する．
	 *
	 * @return このオブジェクトの定義を表すSynsetDefオブジェクト
	 * @throws SQLException データベースへのアクセスにエラーが発生した場合
	 */
	public SynsetDef getDef() throws SQLException{

		return this.parent.findSynsetDef(this);

	}

	/**
	 * このオブジェクトと関連するSenseリストの取得．
	 *
	 * @return このオブジェクトと関連するSenseオブジェクトのリスト
	 * @throws SQLException データベースへのアクセスにエラーが発生した場合
	 */
	public List<Sense> getSenses() throws SQLException{

		return this.parent.findSenses(this);

	}

	/**
	 * 言語を指定して，このオブジェクトと関連するSenseリストを取得する．
	 *
	 * @param lang 取得するSenseの言語
	 * @return このオブジェクトと関連するSenseオブジェクトのリスト
	 * @throws SQLException データベースへのアクセスにエラーが発生した場合
	 */
	public List<Sense> getSenses(final Lang lang) throws SQLException{
		assert lang != null : "lang is null";

		return this.parent.findSenses(this, lang);

	}

	/**
	 * このSynsetから出ているリンク集合を取得する．
	 *
	 * @return このSynsetから出ているリンク集合
	 * @throws SQLException データベースへのアクセスにエラーが発生した場合
	 */
	public List<Synlink> getSynlinks() throws SQLException{

		return this.parent.findSynlinks(this);

	}

	/**
	 * リンクの種類を指定して，このSynsetから出ているリンク集合を取得する．
	 *
	 * @param link 取得するリンクの種類
	 * @return このSynsetから出ているリンク集合
	 * @throws SQLException データベースへのアクセスにエラーが発生した場合
	 */
	public List<Synlink> getSynlinks(final Link link) throws SQLException{
		assert link != null : "link is null";

		return this.parent.findSynlinks(this, link);

	}

	/**
	 * このオブジェクトと指定されたリンクで結ばれているSynset集合を取得する．
	 *
	 * @param link 取得するリンクの種類
	 * @return 指定されたリンクで結ばれているSynsetのリスト
	 * @throws SQLException データベースへのアクセスにエラーが発生した場合
	 */
	public List<Synset> getConnectedSynsets(final Link link) throws SQLException{
		assert link != null : "link is null";

		return this.parent.findSynsets(this, link);

	}

	/**
	 * このオブジェクトと指定されたオブジェクトを比較します．
	 * このメソッドは，SynsetIDが等しい場合にtrueを返します．
	 *
	 * @param obj このオブジェクトと比較されるオブジェクト
	 * @return 比較対象のオブジェクトとSynsetIDが等しい場合はtrue, そうでない場合はfalse
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

	/**
	 * このオブジェクトのハッシュ値を取得する．
	 * ハッシュ値は，SynsetIDのみから計算されます．
	 *
	 * @return このオブジェクトのハッシュ値
	 */
	@Override
	public int hashCode() {

		return this.getSynsetID().hashCode();

	}

	/**
	 * このオブジェクトのJSON表記を取得する．
	 *
	 * @return このオブジェクトをJSON形式で表した文字列．
	 */
	@Override
	public String toString() {

		return String.format("{\"synsetid\": \"%s\", \"pos\": \"%s\", \"name\": \"%s\", \"src\": \"%s\"}",
				this.getSynsetID(),
				this.getPos(),
				this.getName().replace("\"", "\\\""),
				this.getSrc().replace("\"", "\\\""));

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
