C:
cd C:\Users\admin\Desktop\github\Pay\trunk\PayServer\protobuf
protoc --java_out=./ AppProto.proto
protoc --java_out=./ PCErrorProto.proto
protoc --java_out=./ OrderRecordProto.proto
protoc --java_out=./ PayProto.proto
protoc --java_out=./ LoginProto.proto
protoc --java_out=./ AddNotify.proto
pause
