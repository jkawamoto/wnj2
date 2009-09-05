package org.wnj2;

class LazyWord extends Word{

	private Word impl = null;

	LazyWord(final Wnj2 parent, final int wordid){
		super(parent, wordid);

	}

	/* (非 Javadoc)
	 * @see org.wnj2.Word#getLang()
	 */
	@Override
	public Lang getLang() {

		return this.getImpl().getLang();

	}

	/* (非 Javadoc)
	 * @see org.wnj2.Word#getLemma()
	 */
	@Override
	public String getLemma() {

		return this.getImpl().getLemma();

	}

	/* (非 Javadoc)
	 * @see org.wnj2.Word#getPos()
	 */
	@Override
	public Pos getPos() {

		return this.getImpl().getPos();

	}

	/* (非 Javadoc)
	 * @see org.wnj2.Word#getPron()
	 */
	@Override
	public String getPron() {

		return this.getImpl().getPron();

	}

	protected Word getImpl(){

		if(this.impl == null){

			this.impl = this.parent.findWord(this);

		}

		return this.impl;

	}

}
