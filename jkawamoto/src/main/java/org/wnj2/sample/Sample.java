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

package org.wnj2.sample;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import org.wnj2.Link;
import org.wnj2.Pos;
import org.wnj2.Synset;
import org.wnj2.Wnj2;

public class Sample {

	public static void main(final String[] args) {

		try {

			final Wnj2 wn = new Wnj2(new File("./db/wnjpn.db"));
			// Synsetを取得します．
			for(Synset s : wn.findSynsets(args[0], Pos.n)){

			    // 下位語を取得して表示します
			    for(Synset h : s.getConnectedSynsets(Link.hypo)){

			        System.out.println(h);

			    }

			}

			wn.close();

		} catch (FileNotFoundException e) {

			e.printStackTrace();

		} catch (ClassNotFoundException e) {

			e.printStackTrace();

		} catch (SQLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}

	}

}
