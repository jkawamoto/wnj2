package org.wnj2;

class LazySynset extends Synset{

	private Synset impl = null;

	LazySynset(final Wnj2 parent, final String id){
		super(parent, id);

	}

	/* (非 Javadoc)
	 * @see org.wnj2.Synset#getName()
	 */
	@Override
	public String getName() {

		return this.getImpl().getName();

	}

	/* (非 Javadoc)
	 * @see org.wnj2.Synset#getPos()
	 */
	@Override
	public Pos getPos() {

		return this.getImpl().getPos();

	}

	/* (非 Javadoc)
	 * @see org.wnj2.Synset#getSrc()
	 */
	@Override
	public String getSrc() {

		return this.getImpl().getSrc();

	}

	protected Synset getImpl(){

		if(this.impl == null){

			this.impl = this.parent.findSynset(this);

		}
		return this.impl;

	}

}
