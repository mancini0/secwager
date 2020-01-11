/**
 * @fileoverview gRPC-Web generated client stub for secwager
 * @enhanceable
 * @public
 */

// GENERATED CODE -- DO NOT EDIT!



const grpc = {};
grpc.web = require('grpc-web');


var proto_market_pb = require('../proto/market_pb.js')
const proto = {};
proto.secwager = require('./market_data_pb.js');

/**
 * @param {string} hostname
 * @param {?Object} credentials
 * @param {?Object} options
 * @constructor
 * @struct
 * @final
 */
proto.secwager.MarketDataServiceClient =
    function(hostname, credentials, options) {
  if (!options) options = {};
  options['format'] = 'text';

  /**
   * @private @const {!grpc.web.GrpcWebClientBase} The client
   */
  this.client_ = new grpc.web.GrpcWebClientBase(options);

  /**
   * @private @const {string} The hostname
   */
  this.hostname_ = hostname;

};


/**
 * @param {string} hostname
 * @param {?Object} credentials
 * @param {?Object} options
 * @constructor
 * @struct
 * @final
 */
proto.secwager.MarketDataServicePromiseClient =
    function(hostname, credentials, options) {
  if (!options) options = {};
  options['format'] = 'text';

  /**
   * @private @const {!grpc.web.GrpcWebClientBase} The client
   */
  this.client_ = new grpc.web.GrpcWebClientBase(options);

  /**
   * @private @const {string} The hostname
   */
  this.hostname_ = hostname;

};


/**
 * @const
 * @type {!grpc.web.MethodDescriptor<
 *   !proto.secwager.MarketDataRequest,
 *   !proto.secwager.MarketDataResponse>}
 */
const methodDescriptor_MarketDataService_subscribeToMarketData = new grpc.web.MethodDescriptor(
  '/secwager.MarketDataService/subscribeToMarketData',
  grpc.web.MethodType.SERVER_STREAMING,
  proto.secwager.MarketDataRequest,
  proto.secwager.MarketDataResponse,
  /**
   * @param {!proto.secwager.MarketDataRequest} request
   * @return {!Uint8Array}
   */
  function(request) {
    return request.serializeBinary();
  },
  proto.secwager.MarketDataResponse.deserializeBinary
);


/**
 * @const
 * @type {!grpc.web.AbstractClientBase.MethodInfo<
 *   !proto.secwager.MarketDataRequest,
 *   !proto.secwager.MarketDataResponse>}
 */
const methodInfo_MarketDataService_subscribeToMarketData = new grpc.web.AbstractClientBase.MethodInfo(
  proto.secwager.MarketDataResponse,
  /**
   * @param {!proto.secwager.MarketDataRequest} request
   * @return {!Uint8Array}
   */
  function(request) {
    return request.serializeBinary();
  },
  proto.secwager.MarketDataResponse.deserializeBinary
);


/**
 * @param {!proto.secwager.MarketDataRequest} request The request proto
 * @param {?Object<string, string>} metadata User defined
 *     call metadata
 * @return {!grpc.web.ClientReadableStream<!proto.secwager.MarketDataResponse>}
 *     The XHR Node Readable Stream
 */
proto.secwager.MarketDataServiceClient.prototype.subscribeToMarketData =
    function(request, metadata) {
  return this.client_.serverStreaming(this.hostname_ +
      '/secwager.MarketDataService/subscribeToMarketData',
      request,
      metadata || {},
      methodDescriptor_MarketDataService_subscribeToMarketData);
};


/**
 * @param {!proto.secwager.MarketDataRequest} request The request proto
 * @param {?Object<string, string>} metadata User defined
 *     call metadata
 * @return {!grpc.web.ClientReadableStream<!proto.secwager.MarketDataResponse>}
 *     The XHR Node Readable Stream
 */
proto.secwager.MarketDataServicePromiseClient.prototype.subscribeToMarketData =
    function(request, metadata) {
  return this.client_.serverStreaming(this.hostname_ +
      '/secwager.MarketDataService/subscribeToMarketData',
      request,
      metadata || {},
      methodDescriptor_MarketDataService_subscribeToMarketData);
};


module.exports = proto.secwager;

