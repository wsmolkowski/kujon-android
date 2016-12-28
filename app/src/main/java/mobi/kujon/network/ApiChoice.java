package mobi.kujon.network;


public interface ApiChoice {
    void setApiType(@ApiType.ApiTypes int apiType);
    @ApiType.ApiTypes int getApiType();
}
