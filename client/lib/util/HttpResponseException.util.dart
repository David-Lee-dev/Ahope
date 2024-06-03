import 'package:client/enum/errorCode.enum.dart';
import 'package:client/enum/requestMethod.enum.dart';

class HttpResponseException implements Exception {
  late ErrorCode code;
  late RequestMethod method;

  HttpResponseException(RequestMethod requestMethod, int statusCode) {
    switch (statusCode) {
      case 400:
        code = ErrorCode.badRequest;
        break;
      case 404:
        code = ErrorCode.notFound;
        break;
      default:
        code = ErrorCode.serverError;
        break;
    }

    method = requestMethod;
  }
}
