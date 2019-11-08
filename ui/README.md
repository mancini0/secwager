protoc -I=~/secwager proto/order_entry.proto  \
--js_out=import_style=commonjs:./ui/staging \
--grpc-web_out=import_style=commonjs,mode=grpcwebtext:./ui/staging

docker build . -t secwager-ui:dev
docker tag secwager-ui:dev us.gcr.io/secwager/secwager-ui:dev 
docker push us.gcr.io/secwager/secwager-ui:dev 
kubectl apply -f k8s.yaml 

