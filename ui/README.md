protoc -I=~/secwager proto/order_entry.proto  \
--js_out=import_style=commonjs:./ui/staging \
--grpc-web_out=import_style=commonjs,mode=grpcwebtext:./ui/staging