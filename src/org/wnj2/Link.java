package org.wnj2;

public enum Link {

	/** See also */
	also("See also"),

	/** Synonyms */
	syns("Synonyms"),

	/** Hypemyms */
	hype("Hypemyms"),

	/** Instance */
	inst("Instance"),

	/** Hyponym */
	hypo("Hyponym"),

	/** Has Instance */
	hasi("Has Instance"),

	/** Meronyms */
	mero("Meronyms"),

	/** Meronyms - Member */
	mmem("Meronyms - Member"),

	/** Meronyms - Substance */
	msub("Meronyms - Substance"),

	/** Meronyms - Part */
	mprt("Meronyms - Part"),

	/** Holonyms */
	holo("Holonyms"),

	/** Holonyms - Member */
	hmem("Holonyms - Member"),

	/** Holonyms - Substance */
	hsub("Holonyms - Substance"),

	/** Holonyms - Part */
	hprt("Holonyms - Part"),

	/** Attributes */
	attr("Attributes"),

	/** Similar to */
	sim("Similar to"),

	/** Entails */
	enta("Entails"),

	/** Causes */
	caus("Causes"),

	/** Domain - Category */
	dmnc("Domain - Category"),

	/** Domain - Usage */
	dmnu("Domain - Usage"),

	/** Domain - Region */
	dmnr("Domain - Region"),

	/** In Domain - Category */
	dmtc("In Domain - Category"),

	/** In Domain - Usage */
	dmtu("In Domain - Usage"),

	/** In Domain - Region */
	dmtr("In Domain - Region"),

	/** Antonyms */
	ants("Antonyms");


	public final String description;

	private Link(final String description){

		this.description = description;

	}

	public String getDescription(){

		return this.description;

	}

}
