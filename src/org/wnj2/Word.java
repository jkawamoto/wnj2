package org.wnj2;

import java.util.List;


public abstract class Word {

	protected final Wnj2 parent;
	private final int wordid;

	/////////////////////////////////////////////////////////////////////////////////////
	// Constractor
	/////////////////////////////////////////////////////////////////////////////////////
	protected Word(final Wnj2 parent, final int wordid){

		this.parent = parent;
		this.wordid = wordid;

	}

	/////////////////////////////////////////////////////////////////////////////////////
	// Public methods
	/////////////////////////////////////////////////////////////////////////////////////
	public int getWordID(){

		return this.wordid;

	}

	public abstract Lang getLang();
	public abstract String getLemma();
	public abstract String getPron();
	public abstract Pos getPos();

	public List<Sense> getSenses(){

		return this.parent.findSenses(this);

	}

	public List<Synset> getSynsets(){

		return this.parent.findSynsets(this.getLemma(), this.getPos());

	}



	@Override
	public boolean equals(final Object obj){

		try{

			final Word that = (Word)obj;
			return this.getWordID() == that.getWordID();

		}catch(final ClassCastException e){

			return false;

		}

	}

	@Override
	public int hashCode() {

		return Integer.valueOf(this.getWordID()).hashCode();

	}

	@Override
	public String toString() {
		// TODO 自動生成されたメソッド・スタブ
		return super.toString();
	}

	/////////////////////////////////////////////////////////////////////////////////////
	// Static methods
	/////////////////////////////////////////////////////////////////////////////////////
	static Word create(final Wnj2 parent, final int id){

		return new LazyWord(parent, id);

	}

	static Word create(final Wnj2 parent, final int id, final Lang lang, final String lemma, final String pron, final String pos){

		return new InitializedWord(parent, id, lang, lemma, pron, pos);

	}

}
