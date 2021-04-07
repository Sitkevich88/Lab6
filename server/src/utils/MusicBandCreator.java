package utils;

import data.*;


public class MusicBandCreator {

    private InputFromBufferValidator checker = new InputFromBufferValidator();

    /**
     * Executes the command.
     * @return MusicBand new band
     */

    public MusicBand getMusicBandFromScriptInBuffer(){

        MusicBand musicBand = null;
        ProtoMusicBandCreator protoMusicBandCreator = new ProtoMusicBandCreator();
        ProtoMusicBand protoMusicBand = protoMusicBandCreator.getProtoMusicBandFromScriptInBuffer();
        if (protoMusicBand!=null){
            musicBand = new MusicBand(protoMusicBand);
        }
        return musicBand;

    }
}
