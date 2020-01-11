/**
 * @fileoverview gRPC-Web generated client stub for secwager.orderentry
 * @enhanceable
 * @public
 */

// GENERATED CODE -- DO NOT EDIT!



const grpc = {};
grpc.web = require('grpc-web');


var proto_market_pb = require('../proto/market_pb.js')
const proto = {};
proto.secwager = {};
proto.secwager.orderentry = require('./order_entry_pb.js');

/**
 * @param {string} hostname
 * @param {?Object} credentials
 * @param {?Object} options
 * @constructor
 * @struct
 * @final
 */
proto.secwager.orderentry.OrderEntryServiceClient =
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
proto.secwager.orderentry.OrderEntryServicePromiseClient =
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
 *   !proto.secwager.orderentry.SubmitOrderRequest,
 *   !proto.secwager.orderentry.SubmitOrderResponse>}
 */
const methodDescriptor_OrderEntryService_SubmitOrder = new grpc.web.MethodDescriptor(
  '/secwager.orderentry.OrderEntryService/SubmitOrder',
  grpc.web.MethodType.UNARY,
  proto.secwager.orderentry.SubmitOrderRequest,
  proto.secwager.orderentry.SubmitOrderResponse,
  /**
   * @param {!proto.secwager.orderentry.SubmitOrderRequest} request
   * @return {!Uint8Array}
   */
  function(request) {
    return request.serializeBinary();
  },
  proto.secwager.orderentry.SubmitOrderResponse.deserializeBinary
);


/**
 * @const
 * @type {!grpc.web.AbstractClientBase.MethodInfo<
 *   !proto.secwager.orderentry.SubmitOrderRequest,
 *   !proto.secwager.orderentry.SubmitOrderResponse>}
 */
const methodInfo_OrderEntryService_SubmitOrder = new grpc.web.AbstractClientBase.MethodInfo(
  proto.secwager.orderentry.SubmitOrderResponse,
  /**
   * @param {!proto.secwager.orderentry.SubmitOrderRequest} request
   * @return {!Uint8Array}
   */
  function(request) {
    return request.serializeBinary();
  },
  proto.secwager.orderentry.SubmitOrderResponse.deserializeBinary
);


/**
 * @param {!proto.secwager.orderentry.SubmitOrderRequest} request The
 *     request proto
 * @param {?Object<string, string>} metadata User defined
 *     call metadata
 * @param {function(?grpc.web.Error, ?proto.secwager.orderentry.SubmitOrderResponse)}
 *     callback The callback function(error, response)
 * @return {!grpc.web.ClientReadableStream<!proto.secwager.orderentry.SubmitOrderResponse>|undefined}
 *     The XHR Node Readable Stream
 */
proto.secwager.orderentry.OrderEntryServiceClient.prototype.submitOrder =
    function(request, metadata, callback) {
  return this.client_.rpcCall(this.hostname_ +
      '/secwager.orderentry.OrderEntryService/SubmitOrder',
      request,
      metadata || {},
      methodDescriptor_OrderEntryService_SubmitOrder,
      callback);
};


/**
 * @param {!proto.secwager.orderentry.SubmitOrderRequest} request The
 *     request proto
 * @param {?Object<string, string>} metadata User defined
 *     call metadata
 * @return {!Promise<!proto.secwager.orderentry.SubmitOrderResponse>}
 *     A native promise that resolves to the response
 */
proto.secwager.orderentry.OrderEntryServicePromiseClient.prototype.submitOrder =
    function(request, metadata) {
  return this.client_.unaryCall(this.hostname_ +
      '/secwager.orderentry.OrderEntryService/SubmitOrder',
      request,
      metadata || {},
      methodDescriptor_OrderEntryService_SubmitOrder);
};


module.exports = proto.secwager.orderentry;

