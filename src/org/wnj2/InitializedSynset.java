package org.wnj2;

class InitializedSynset extends Synset{

	private final Pos pos;
	private final String name;
	private final String src;

	InitializedSynset(final Wnj2 parent, final String id, final Pos pos, final String name, final String src){
		super(parent, id);

		this.pos = pos;
		this.name = name;
		this.src = src;

	}

	/* (非 Javadoc)
	 * @see org.wnj2.Synset#getPos()
	 */
	@Override
	public Pos getPos() {
		return this.pos;
	}

	/* (非 Javadoc)
	 * @see org.wnj2.Synset#getName()
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/* (非 Javadoc)
	 * @see org.wnj2.Synset#getSrc()
	 */
	@Override
	public String getSrc() {
		return this.src;
	}

}
