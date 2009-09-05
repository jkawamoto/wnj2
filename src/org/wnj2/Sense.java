package org.wnj2;

public class Sense {

	protected final Wnj2 parent;

	private final Synset synset;
	private final Word word;
	private final String lang;
	private final int rank;
	private final int lexid;
	private final int freq;
	private final String src;

	Sense(final Wnj2 parent, final String synset, final int word, final String lang, final int rank, final int lex, final int freq, final String src){

		this.parent = parent;
		this.synset = Synset.create(parent, synset);
		this.word = Word.create(parent, word);
		this.lang = lang;
		this.rank = rank;
		this.lexid = lex;
		this.freq = freq;
		this.src = src;

	}

	public Synset getSynset() {
		return this.synset;
	}

	public Word getWord(){
		return this.word;
	}

	public String getLang() {
		return this.lang;
	}

	public int getRank() {
		return this.rank;
	}

	public int getLexID() {
		return this.lexid;
	}

	public int getFreq() {
		return this.freq;
	}

	public String getSrc() {
		return this.src;
	}

	@Override
	public boolean equals(final Object obj){

		if(obj instanceof Sense){

			final Sense that = (Sense)obj;
			return this.getSynset().equals(that.getSynset()) && this.getWord().equals(that.getWord());

		}

		return super.equals(obj);

	}

	@Override
	public int hashCode(){

		return this.getSynset().hashCode() * this.getWord().hashCode();

	}

	@Override
	public String toString() {
		// TODO 自動生成されたメソッド・スタブ
		return super.toString();
	}

}
