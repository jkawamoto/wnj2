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

	public static void main(String[] args) {

		try {

			final Wnj2 wn = new Wnj2(new File("./db/wnjpn.db"));
			// Synsetを取得します．
			for(Synset s : wn.findSynsets("path", Pos.n)){

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
