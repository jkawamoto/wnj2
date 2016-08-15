# Japanese WordNet wrapper for Java
## Wnj2インスタンスの作成
Wnj2インスタンスの作成方法は，[日本語 WordNet](http://nlpwww.nict.go.jp/wn-ja/index.ja.html) の
データファイルを指定する方法とデータベースへのコネクションを指定する方法の二種類があります．
どちらの方法で作成した場合も，使用後は close メソッドを呼び出して下さい．データベースへの接続を切ります．


## データファイルを指定する
[日本語 WordNet ウェブサイト](http://nlpwww.nict.go.jp/wn-ja/index.ja.html) で配布されている SQLite 用データファイルを指定します．

```java
File file = new File("path_to_the_data_file");
Wnj2 wn = new Wnj2(file);
```

この方法を使用する場合，SQLite 用 JDBC ドライバが必要になります．

## データベースコネクションを指定する
こちらは，主に SQLite 以外の DBMS にデータを格納している場合に用います．
配布されている SQLite 用データを他の DBMS へ移行するツールは
[こちら](http://sourceforge.jp/projects/wnj2/releases/) で配布予定です．

例えば MySQL を使用する場合は次のようになるでしょう（環境によって変わります）．

```java
Class.forName("com.mysql.jdbc.Driver");
Connection con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/wnj", "wnj", "pass2wnj");

Wnj2 wn = new Wnj2(con);
```

# License
This software is released under The GNU Lesser General Public License Version 3, see [COPYING.LESSER](COPYING.LESSER) for more detail.
