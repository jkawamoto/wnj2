package org.wnj2;

public class SynsetDef {

	protected final Wnj2 parent;
	private final Synset synset;
	private final Lang lang;
	private final String def;
	private final int sid;

	SynsetDef(final Wnj2 parent, final Synset synset, final Lang lang, final String def, final int sid){

		this.parent = parent;
		this.synset = synset;
		this.lang = lang;
		this.def = def;
		this.sid = sid;

	}

	public Synset getSynset() {
		return this.synset;
	}

	public Lang getLang() {
		return this.lang;
	}

	public String getDef() {
		return this.def;
	}

	public int getSID() {
		return this.sid;
	}

	@Override
	public boolean equals(Object obj) {

		if(obj instanceof SynsetDef){

			final SynsetDef that = (SynsetDef)obj;
			return this.getSynset().equals(that.getSynset());

		}

		return super.equals(obj);
	}

	@Override
	public int hashCode() {

		return this.getSynset().hashCode();

	}

	@Override
	public String toString() {
		// TODO 自動生成されたメソッド・スタブ
		return super.toString();
	}

}
