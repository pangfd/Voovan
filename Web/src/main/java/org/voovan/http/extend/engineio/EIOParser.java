package org.voovan.http.extend.engineio;

import org.voovan.http.extend.SocketIOParserException;
import org.voovan.tools.TString;
import org.voovan.tools.log.Logger;

/**
 * engine.io 报文解析
 *
 * @author: helyho
 * Voovan Framework.
 * WebSite: https://github.com/helyho/Voovan
 * Licence: Apache v2 License
 */
public class EIOParser {

    /**
     * 判断是否是 socket.io 的消息
     * @param msg 消息
     * @return true: 是, false: 否
     */
    public static boolean isEngineIOMessage(String msg){
        return TString.regexMatch(msg, "^[0-6]") >0;
    }

    /**
     * 解析 engine.io 报文
     * @param msg 报文内容
     * @return EIOPacket 对象
     * @throws SocketIOParserException 解析异常
     */
    public static EIOPacket decode(String msg) throws SocketIOParserException {
        EIOPacket engineIOPacket = new EIOPacket();
        char c = msg.charAt(0);
        int engineType = c - 48;
        if(!TString.isNumber(String.valueOf(c),10)){
            throw new SocketIOParserException("The engine.io packet first char must be number");
        }

        if(engineType<0 || engineType>6) {
            throw new SocketIOParserException("The engine.io packet first char must be exists in [0...6]");
        }

        engineIOPacket.setEngineType(c-48);
        engineIOPacket.setData(msg.substring(1));

        return engineIOPacket;
    }

    /**
     * 编码 Packet 成 字符串
     * @param packet SIOPacket 对象
     * @return 字符串
     */
    public static String encode(EIOPacket packet){
        return packet.getEngineType() + packet.getData();
    }

    public static void main(String[] args) throws SocketIOParserException {
        String mm = "42/socketio,0[\"show\", \"kkkk\"]";
        Logger.simple(EIOParser.isEngineIOMessage(mm));
        EIOPacket packet = EIOParser.decode(mm);
        Logger.simple(EIOParser.encode(packet));
    }
}
