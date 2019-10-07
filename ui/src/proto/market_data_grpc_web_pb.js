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

  /**
   * @private @const {?Object} The credentials to be used to connect
   *    to the server
   */
  this.credentials_ = credentials;

  /**
   * @private @const {?Object} Options for the client
   */
  this.options_ = options;
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

  /**
   * @private @const {?Object} The credentials to be used to connect
   *    to the server
   */
  this.credentials_ = credentials;

  /**
   * @private @const {?Object} Options for the client
   */
  this.options_ = options;
};


/**
 * @const
 * @type {!grpc.web.MethodDescriptor<
 *   !proto.secwager.QuoteRequest,
 *   !proto.secwager.QuoteResponse>}
 */
const methodDescriptor_MarketDataService_streamQuotes = new grpc.web.MethodDescriptor(
  '/secwager.MarketDataService/streamQuotes',
  grpc.web.MethodType.SERVER_STREAMING,
  proto.secwager.QuoteRequest,
  proto.secwager.QuoteResponse,
  /** @param {!proto.secwager.QuoteRequest} request */
  function(request) {
    return request.serializeBinary();
  },
  proto.secwager.QuoteResponse.deserializeBinary
);


/**
 * @const
 * @type {!grpc.web.AbstractClientBase.MethodInfo<
 *   !proto.secwager.QuoteRequest,
 *   !proto.secwager.QuoteResponse>}
 */
const methodInfo_MarketDataService_streamQuotes = new grpc.web.AbstractClientBase.MethodInfo(
  proto.secwager.QuoteResponse,
  /** @param {!proto.secwager.QuoteRequest} request */
  function(request) {
    return request.serializeBinary();
  },
  proto.secwager.QuoteResponse.deserializeBinary
);


/**
 * @param {!proto.secwager.QuoteRequest} request The request proto
 * @param {?Object<string, string>} metadata User defined
 *     call metadata
 * @return {!grpc.web.ClientReadableStream<!proto.secwager.QuoteResponse>}
 *     The XHR Node Readable Stream
 */
proto.secwager.MarketDataServiceClient.prototype.streamQuotes =
    function(request, metadata) {
  return this.client_.serverStreaming(this.hostname_ +
      '/secwager.MarketDataService/streamQuotes',
      request,
      metadata || {},
      methodDescriptor_MarketDataService_streamQuotes);
};


/**
 * @param {!proto.secwager.QuoteRequest} request The request proto
 * @param {?Object<string, string>} metadata User defined
 *     call metadata
 * @return {!grpc.web.ClientReadableStream<!proto.secwager.QuoteResponse>}
 *     The XHR Node Readable Stream
 */
proto.secwager.MarketDataServicePromiseClient.prototype.streamQuotes =
    function(request, metadata) {
  return this.client_.serverStreaming(this.hostname_ +
      '/secwager.MarketDataService/streamQuotes',
      request,
      metadata || {},
      methodDescriptor_MarketDataService_streamQuotes);
};


/**
 * @const
 * @type {!grpc.web.MethodDescriptor<
 *   !proto.secwager.DepthRequest,
 *   !proto.secwager.DepthResponse>}
 */
const methodDescriptor_MarketDataService_streamDepth = new grpc.web.MethodDescriptor(
  '/secwager.MarketDataService/streamDepth',
  grpc.web.MethodType.SERVER_STREAMING,
  proto.secwager.DepthRequest,
  proto.secwager.DepthResponse,
  /** @param {!proto.secwager.DepthRequest} request */
  function(request) {
    return request.serializeBinary();
  },
  proto.secwager.DepthResponse.deserializeBinary
);


/**
 * @const
 * @type {!grpc.web.AbstractClientBase.MethodInfo<
 *   !proto.secwager.DepthRequest,
 *   !proto.secwager.DepthResponse>}
 */
const methodInfo_MarketDataService_streamDepth = new grpc.web.AbstractClientBase.MethodInfo(
  proto.secwager.DepthResponse,
  /** @param {!proto.secwager.DepthRequest} request */
  function(request) {
    return request.serializeBinary();
  },
  proto.secwager.DepthResponse.deserializeBinary
);


/**
 * @param {!proto.secwager.DepthRequest} request The request proto
 * @param {?Object<string, string>} metadata User defined
 *     call metadata
 * @return {!grpc.web.ClientReadableStream<!proto.secwager.DepthResponse>}
 *     The XHR Node Readable Stream
 */
proto.secwager.MarketDataServiceClient.prototype.streamDepth =
    function(request, metadata) {
  return this.client_.serverStreaming(this.hostname_ +
      '/secwager.MarketDataService/streamDepth',
      request,
      metadata || {},
      methodDescriptor_MarketDataService_streamDepth);
};


/**
 * @param {!proto.secwager.DepthRequest} request The request proto
 * @param {?Object<string, string>} metadata User defined
 *     call metadata
 * @return {!grpc.web.ClientReadableStream<!proto.secwager.DepthResponse>}
 *     The XHR Node Readable Stream
 */
proto.secwager.MarketDataServicePromiseClient.prototype.streamDepth =
    function(request, metadata) {
  return this.client_.serverStreaming(this.hostname_ +
      '/secwager.MarketDataService/streamDepth',
      request,
      metadata || {},
      methodDescriptor_MarketDataService_streamDepth);
};


/**
 * @const
 * @type {!grpc.web.MethodDescriptor<
 *   !proto.secwager.InstrumentRequest,
 *   !proto.secwager.InstrumentResponse>}
 */
const methodDescriptor_MarketDataService_getInstruments = new grpc.web.MethodDescriptor(
  '/secwager.MarketDataService/getInstruments',
  grpc.web.MethodType.UNARY,
  proto.secwager.InstrumentRequest,
  proto.secwager.InstrumentResponse,
  /** @param {!proto.secwager.InstrumentRequest} request */
  function(request) {
    return request.serializeBinary();
  },
  proto.secwager.InstrumentResponse.deserializeBinary
);


/**
 * @const
 * @type {!grpc.web.AbstractClientBase.MethodInfo<
 *   !proto.secwager.InstrumentRequest,
 *   !proto.secwager.InstrumentResponse>}
 */
const methodInfo_MarketDataService_getInstruments = new grpc.web.AbstractClientBase.MethodInfo(
  proto.secwager.InstrumentResponse,
  /** @param {!proto.secwager.InstrumentRequest} request */
  function(request) {
    return request.serializeBinary();
  },
  proto.secwager.InstrumentResponse.deserializeBinary
);


/**
 * @param {!proto.secwager.InstrumentRequest} request The
 *     request proto
 * @param {?Object<string, string>} metadata User defined
 *     call metadata
 * @param {function(?grpc.web.Error, ?proto.secwager.InstrumentResponse)}
 *     callback The callback function(error, response)
 * @return {!grpc.web.ClientReadableStream<!proto.secwager.InstrumentResponse>|undefined}
 *     The XHR Node Readable Stream
 */
proto.secwager.MarketDataServiceClient.prototype.getInstruments =
    function(request, metadata, callback) {
  return this.client_.rpcCall(this.hostname_ +
      '/secwager.MarketDataService/getInstruments',
      request,
      metadata || {},
      methodDescriptor_MarketDataService_getInstruments,
      callback);
};


/**
 * @param {!proto.secwager.InstrumentRequest} request The
 *     request proto
 * @param {?Object<string, string>} metadata User defined
 *     call metadata
 * @return {!Promise<!proto.secwager.InstrumentResponse>}
 *     A native promise that resolves to the response
 */
proto.secwager.MarketDataServicePromiseClient.prototype.getInstruments =
    function(request, metadata) {
  return this.client_.unaryCall(this.hostname_ +
      '/secwager.MarketDataService/getInstruments',
      request,
      metadata || {},
      methodDescriptor_MarketDataService_getInstruments);
};


module.exports = proto.secwager;

