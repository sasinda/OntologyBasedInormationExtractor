package com.sigmacr.ling;

/**
 * Created with IntelliJ IDEA.
 * User: Sasinda
 * Date: 8/15/13
 * Time: 12:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class SynonymRanks {

    final static String TYPE_SynInclude = "synInclude";
    final static String TYPE_P1Syn = "p1Syn";
    final static String TYPE_P2Syn = "p2Syn";
    final static String TYPE_P3Syn = "p3Syn";
    final static String TYPE_DerivationalyRelatedFrom = "derivationallyRelatedFrom";
    final static String TYPE_P1DRel = "p1DRel";
    final static String TYPE_P2DRel = "p2DRel";

    final static int RANK_SynInclude = 100;
    final static int RANK_P1Syn = 90;
    final static int RANK_P2Syn = 90;
    final static int RANK_P3Syn = 50;
    final static int RANK_DerivationalyRelatedFrom = 80;
    final static int RANK_P1DRel = 70;
    final static int RANK_P2DRel = 65;

    public int getRank(String uri) {
        if (uri.startsWith("http")) {
            uri = uri.substring(uri.indexOf('#'));
        }
        if (uri.equals(TYPE_SynInclude)) {
            return RANK_SynInclude;
        } else if (uri.equals(TYPE_P1Syn)) {
            return RANK_P1Syn;
        } else if (uri.equals(TYPE_P2Syn)) {
            return RANK_P2Syn;
        } else if (uri.equals(TYPE_P3Syn)) {
            return RANK_P3Syn;
        } else if (uri.equals(TYPE_DerivationalyRelatedFrom)) {
            return RANK_DerivationalyRelatedFrom;
        } else if (uri.equals(TYPE_P1DRel)) {
            return RANK_P1DRel;
        } else if (uri.equals(TYPE_P2DRel)) {
            return RANK_P2DRel;
        }
        throw new IllegalArgumentException(uri+" is not ranked in the list");
    }

}
