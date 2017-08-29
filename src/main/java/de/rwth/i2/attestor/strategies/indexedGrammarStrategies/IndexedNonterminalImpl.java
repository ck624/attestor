package de.rwth.i2.attestor.strategies.indexedGrammarStrategies;

import de.rwth.i2.attestor.graph.BasicNonterminal;
import de.rwth.i2.attestor.graph.digraph.NodeLabel;
import de.rwth.i2.attestor.strategies.indexedGrammarStrategies.index.Index;
import de.rwth.i2.attestor.strategies.indexedGrammarStrategies.index.IndexSymbol;

import java.util.List;

public class IndexedNonterminalImpl implements IndexedNonterminal {


	protected Index index;
	protected  final BasicNonterminal basicNonterminal;

	public IndexedNonterminalImpl(String label,
                                  int rank,
                                  boolean[] isReductionTentacle,
                                  List<IndexSymbol> index){

		this.basicNonterminal = BasicNonterminal.getNonterminal( label, rank, isReductionTentacle );

		this.index = new Index(index);
	}


	public IndexedNonterminalImpl(String label, List<IndexSymbol> index ){
		this.basicNonterminal = BasicNonterminal.getNonterminal(label);
		this.index = new Index(index);
	}

	private IndexedNonterminalImpl(BasicNonterminal basicNonterminal, List<IndexSymbol> index ){
		this.basicNonterminal = basicNonterminal;

        this.index = new Index(index);
	}

	protected IndexedNonterminalImpl(BasicNonterminal basicNonterminal, Index index) {

	    this.basicNonterminal = basicNonterminal;
	    this.index = index;
    }

    @Override
    public Index getIndex() {
	    return index;
    }

    @Override
    public IndexedNonterminal getWithShortenedIndex(){
	    return new IndexedNonterminalImpl(basicNonterminal, index.getWithShortenedIndex());
	}

	@Override
    public IndexedNonterminal getWithProlongedIndex(IndexSymbol s){

		return new IndexedNonterminalImpl(basicNonterminal, index.getWithProlongedIndex(s));
	}


	@Override
    public IndexedNonterminal getWithProlongedIndex(List<IndexSymbol> postfix){

        return new IndexedNonterminalImpl(basicNonterminal, index.getWithProlongedIndex(postfix));
	}

	@Override
    public IndexedNonterminal getWithIndex(List<IndexSymbol> index){
        return new IndexedNonterminalImpl(basicNonterminal, index);
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((basicNonterminal == null) ? 0 : basicNonterminal.hashCode());

		for(int i = 0; i < index.size(); i++) {
		    IndexSymbol symb = index.get(i);
			result = prime * symb.hashCode();
		}
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IndexedNonterminal other = (IndexedNonterminal) obj;
	    return getLabel().equals(other.getLabel()) && getIndex().equals(other.getIndex());
	}

	@Override
	public boolean matches( NodeLabel obj ){
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IndexedNonterminal other = (IndexedNonterminal) obj;
        return getLabel().equals(other.getLabel());
	}

	@Override
	public int getRank() {
		return basicNonterminal.getRank();
	}

	@Override
	public boolean isReductionTentacle(int tentacle) {
		return basicNonterminal.isReductionTentacle(tentacle);
	}

	@Override
	public void setReductionTentacle( int tentacle ){
		basicNonterminal.setReductionTentacle(tentacle);
	}

	@Override
	public void unsetReductionTentacle( int tentacle ){
		basicNonterminal.unsetReductionTentacle(tentacle);
	}

	@Override
	public String toString(){
		return basicNonterminal.toString() + this.index.toString();
	}

	@Override
	public String getLabel() {
		return basicNonterminal.getLabel();
	}

}
