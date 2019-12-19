
class WeChatWorkAuthResponse {
  final int errCode;
  final String code;
  final String state;

  WeChatWorkAuthResponse.fromMap(Map map)
    : errCode = map['errCode'],
      code = map['code'],
      state = map['state'];
}