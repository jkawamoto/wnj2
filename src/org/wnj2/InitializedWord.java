package org.wnj2;

class InitializedWord extends Word{

	private final Lang lang;
	private final String lemma;
	private final String pron;
	private final Pos pos;

	InitializedWord(final Wnj2 parent, final int id, final Lang lang, final String lemma, final String pron, final String pos){
		super(parent, id);

		this.lang = lang;
		this.lemma = lemma;
		this.pron = pron;
		this.pos = Pos.valueOf(pos);

	}

	/* (非 Javadoc)
	 * @see org.wnj2.Word#getLang()
	 */
	@Override
	public Lang getLang() {
		return this.lang;
	}

	/* (非 Javadoc)
	 * @see org.wnj2.Word#getLemma()
	 */
	@Override
	public String getLemma() {
		return this.lemma;
	}

	/* (非 Javadoc)
	 * @see org.wnj2.Word#getPron()
	 */
	@Override
	public String getPron() {
		return this.pron;
	}

	/* (非 Javadoc)
	 * @see org.wnj2.Word#getPos()
	 */
	@Override
	public Pos getPos() {
		return this.pos;
	}

}
