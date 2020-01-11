// source: proto/market_data.proto
/**
 * @fileoverview
 * @enhanceable
 * @suppress {messageConventions} JS Compiler reports an error if a variable or
 *     field starts with 'MSG_' and isn't a translatable message.
 * @public
 */
// GENERATED CODE -- DO NOT EDIT!

var jspb = require('google-protobuf');
var goog = jspb;
var global = Function('return this')();

var proto_market_pb = require('../proto/market_pb.js');
goog.object.extend(proto, proto_market_pb);
goog.exportSymbol('proto.secwager.Instrument', null, global);
goog.exportSymbol('proto.secwager.League', null, global);
goog.exportSymbol('proto.secwager.MarketDataRequest', null, global);
goog.exportSymbol('proto.secwager.MarketDataResponse', null, global);
/**
 * Generated by JsPbCodeGenerator.
 * @param {Array=} opt_data Optional initial data array, typically from a
 * server response, or constructed directly in Javascript. The array is used
 * in place and becomes part of the constructed object. It is not cloned.
 * If no data is provided, the constructed object will be empty, but still
 * valid.
 * @extends {jspb.Message}
 * @constructor
 */
proto.secwager.Instrument = function(opt_data) {
  jspb.Message.initialize(this, opt_data, 0, -1, null, null);
};
goog.inherits(proto.secwager.Instrument, jspb.Message);
if (goog.DEBUG && !COMPILED) {
  /**
   * @public
   * @override
   */
  proto.secwager.Instrument.displayName = 'proto.secwager.Instrument';
}
/**
 * Generated by JsPbCodeGenerator.
 * @param {Array=} opt_data Optional initial data array, typically from a
 * server response, or constructed directly in Javascript. The array is used
 * in place and becomes part of the constructed object. It is not cloned.
 * If no data is provided, the constructed object will be empty, but still
 * valid.
 * @extends {jspb.Message}
 * @constructor
 */
proto.secwager.MarketDataRequest = function(opt_data) {
  jspb.Message.initialize(this, opt_data, 0, -1, null, null);
};
goog.inherits(proto.secwager.MarketDataRequest, jspb.Message);
if (goog.DEBUG && !COMPILED) {
  /**
   * @public
   * @override
   */
  proto.secwager.MarketDataRequest.displayName = 'proto.secwager.MarketDataRequest';
}
/**
 * Generated by JsPbCodeGenerator.
 * @param {Array=} opt_data Optional initial data array, typically from a
 * server response, or constructed directly in Javascript. The array is used
 * in place and becomes part of the constructed object. It is not cloned.
 * If no data is provided, the constructed object will be empty, but still
 * valid.
 * @extends {jspb.Message}
 * @constructor
 */
proto.secwager.MarketDataResponse = function(opt_data) {
  jspb.Message.initialize(this, opt_data, 0, -1, proto.secwager.MarketDataResponse.repeatedFields_, null);
};
goog.inherits(proto.secwager.MarketDataResponse, jspb.Message);
if (goog.DEBUG && !COMPILED) {
  /**
   * @public
   * @override
   */
  proto.secwager.MarketDataResponse.displayName = 'proto.secwager.MarketDataResponse';
}



if (jspb.Message.GENERATE_TO_OBJECT) {
/**
 * Creates an object representation of this proto.
 * Field names that are reserved in JavaScript and will be renamed to pb_name.
 * Optional fields that are not set will be set to undefined.
 * To access a reserved field use, foo.pb_<name>, eg, foo.pb_default.
 * For the list of reserved names please see:
 *     net/proto2/compiler/js/internal/generator.cc#kKeyword.
 * @param {boolean=} opt_includeInstance Deprecated. whether to include the
 *     JSPB instance for transitional soy proto support:
 *     http://goto/soy-param-migration
 * @return {!Object}
 */
proto.secwager.Instrument.prototype.toObject = function(opt_includeInstance) {
  return proto.secwager.Instrument.toObject(opt_includeInstance, this);
};


/**
 * Static version of the {@see toObject} method.
 * @param {boolean|undefined} includeInstance Deprecated. Whether to include
 *     the JSPB instance for transitional soy proto support:
 *     http://goto/soy-param-migration
 * @param {!proto.secwager.Instrument} msg The msg instance to transform.
 * @return {!Object}
 * @suppress {unusedLocalVariables} f is only used for nested messages
 */
proto.secwager.Instrument.toObject = function(includeInstance, msg) {
  var f, obj = {
    isin: jspb.Message.getFieldWithDefault(msg, 1, ""),
    description: jspb.Message.getFieldWithDefault(msg, 2, ""),
    league: jspb.Message.getFieldWithDefault(msg, 3, 0),
    active: jspb.Message.getBooleanFieldWithDefault(msg, 4, false),
    lastTrade: (f = msg.getLastTrade()) && proto_market_pb.LastTrade.toObject(includeInstance, f),
    depth: (f = msg.getDepth()) && proto_market_pb.DepthBook.toObject(includeInstance, f),
    startTimeEpochSeconds: jspb.Message.getFieldWithDefault(msg, 7, 0)
  };

  if (includeInstance) {
    obj.$jspbMessageInstance = msg;
  }
  return obj;
};
}


/**
 * Deserializes binary data (in protobuf wire format).
 * @param {jspb.ByteSource} bytes The bytes to deserialize.
 * @return {!proto.secwager.Instrument}
 */
proto.secwager.Instrument.deserializeBinary = function(bytes) {
  var reader = new jspb.BinaryReader(bytes);
  var msg = new proto.secwager.Instrument;
  return proto.secwager.Instrument.deserializeBinaryFromReader(msg, reader);
};


/**
 * Deserializes binary data (in protobuf wire format) from the
 * given reader into the given message object.
 * @param {!proto.secwager.Instrument} msg The message object to deserialize into.
 * @param {!jspb.BinaryReader} reader The BinaryReader to use.
 * @return {!proto.secwager.Instrument}
 */
proto.secwager.Instrument.deserializeBinaryFromReader = function(msg, reader) {
  while (reader.nextField()) {
    if (reader.isEndGroup()) {
      break;
    }
    var field = reader.getFieldNumber();
    switch (field) {
    case 1:
      var value = /** @type {string} */ (reader.readString());
      msg.setIsin(value);
      break;
    case 2:
      var value = /** @type {string} */ (reader.readString());
      msg.setDescription(value);
      break;
    case 3:
      var value = /** @type {!proto.secwager.League} */ (reader.readEnum());
      msg.setLeague(value);
      break;
    case 4:
      var value = /** @type {boolean} */ (reader.readBool());
      msg.setActive(value);
      break;
    case 5:
      var value = new proto_market_pb.LastTrade;
      reader.readMessage(value,proto_market_pb.LastTrade.deserializeBinaryFromReader);
      msg.setLastTrade(value);
      break;
    case 6:
      var value = new proto_market_pb.DepthBook;
      reader.readMessage(value,proto_market_pb.DepthBook.deserializeBinaryFromReader);
      msg.setDepth(value);
      break;
    case 7:
      var value = /** @type {number} */ (reader.readUint64());
      msg.setStartTimeEpochSeconds(value);
      break;
    default:
      reader.skipField();
      break;
    }
  }
  return msg;
};


/**
 * Serializes the message to binary data (in protobuf wire format).
 * @return {!Uint8Array}
 */
proto.secwager.Instrument.prototype.serializeBinary = function() {
  var writer = new jspb.BinaryWriter();
  proto.secwager.Instrument.serializeBinaryToWriter(this, writer);
  return writer.getResultBuffer();
};


/**
 * Serializes the given message to binary data (in protobuf wire
 * format), writing to the given BinaryWriter.
 * @param {!proto.secwager.Instrument} message
 * @param {!jspb.BinaryWriter} writer
 * @suppress {unusedLocalVariables} f is only used for nested messages
 */
proto.secwager.Instrument.serializeBinaryToWriter = function(message, writer) {
  var f = undefined;
  f = message.getIsin();
  if (f.length > 0) {
    writer.writeString(
      1,
      f
    );
  }
  f = message.getDescription();
  if (f.length > 0) {
    writer.writeString(
      2,
      f
    );
  }
  f = message.getLeague();
  if (f !== 0.0) {
    writer.writeEnum(
      3,
      f
    );
  }
  f = message.getActive();
  if (f) {
    writer.writeBool(
      4,
      f
    );
  }
  f = message.getLastTrade();
  if (f != null) {
    writer.writeMessage(
      5,
      f,
      proto_market_pb.LastTrade.serializeBinaryToWriter
    );
  }
  f = message.getDepth();
  if (f != null) {
    writer.writeMessage(
      6,
      f,
      proto_market_pb.DepthBook.serializeBinaryToWriter
    );
  }
  f = message.getStartTimeEpochSeconds();
  if (f !== 0) {
    writer.writeUint64(
      7,
      f
    );
  }
};


/**
 * optional string isin = 1;
 * @return {string}
 */
proto.secwager.Instrument.prototype.getIsin = function() {
  return /** @type {string} */ (jspb.Message.getFieldWithDefault(this, 1, ""));
};


/**
 * @param {string} value
 * @return {!proto.secwager.Instrument} returns this
 */
proto.secwager.Instrument.prototype.setIsin = function(value) {
  return jspb.Message.setProto3StringField(this, 1, value);
};


/**
 * optional string description = 2;
 * @return {string}
 */
proto.secwager.Instrument.prototype.getDescription = function() {
  return /** @type {string} */ (jspb.Message.getFieldWithDefault(this, 2, ""));
};


/**
 * @param {string} value
 * @return {!proto.secwager.Instrument} returns this
 */
proto.secwager.Instrument.prototype.setDescription = function(value) {
  return jspb.Message.setProto3StringField(this, 2, value);
};


/**
 * optional League league = 3;
 * @return {!proto.secwager.League}
 */
proto.secwager.Instrument.prototype.getLeague = function() {
  return /** @type {!proto.secwager.League} */ (jspb.Message.getFieldWithDefault(this, 3, 0));
};


/**
 * @param {!proto.secwager.League} value
 * @return {!proto.secwager.Instrument} returns this
 */
proto.secwager.Instrument.prototype.setLeague = function(value) {
  return jspb.Message.setProto3EnumField(this, 3, value);
};


/**
 * optional bool active = 4;
 * @return {boolean}
 */
proto.secwager.Instrument.prototype.getActive = function() {
  return /** @type {boolean} */ (jspb.Message.getBooleanFieldWithDefault(this, 4, false));
};


/**
 * @param {boolean} value
 * @return {!proto.secwager.Instrument} returns this
 */
proto.secwager.Instrument.prototype.setActive = function(value) {
  return jspb.Message.setProto3BooleanField(this, 4, value);
};


/**
 * optional LastTrade last_trade = 5;
 * @return {?proto.secwager.LastTrade}
 */
proto.secwager.Instrument.prototype.getLastTrade = function() {
  return /** @type{?proto.secwager.LastTrade} */ (
    jspb.Message.getWrapperField(this, proto_market_pb.LastTrade, 5));
};


/**
 * @param {?proto.secwager.LastTrade|undefined} value
 * @return {!proto.secwager.Instrument} returns this
*/
proto.secwager.Instrument.prototype.setLastTrade = function(value) {
  return jspb.Message.setWrapperField(this, 5, value);
};


/**
 * Clears the message field making it undefined.
 * @return {!proto.secwager.Instrument} returns this
 */
proto.secwager.Instrument.prototype.clearLastTrade = function() {
  return this.setLastTrade(undefined);
};


/**
 * Returns whether this field is set.
 * @return {boolean}
 */
proto.secwager.Instrument.prototype.hasLastTrade = function() {
  return jspb.Message.getField(this, 5) != null;
};


/**
 * optional DepthBook depth = 6;
 * @return {?proto.secwager.DepthBook}
 */
proto.secwager.Instrument.prototype.getDepth = function() {
  return /** @type{?proto.secwager.DepthBook} */ (
    jspb.Message.getWrapperField(this, proto_market_pb.DepthBook, 6));
};


/**
 * @param {?proto.secwager.DepthBook|undefined} value
 * @return {!proto.secwager.Instrument} returns this
*/
proto.secwager.Instrument.prototype.setDepth = function(value) {
  return jspb.Message.setWrapperField(this, 6, value);
};


/**
 * Clears the message field making it undefined.
 * @return {!proto.secwager.Instrument} returns this
 */
proto.secwager.Instrument.prototype.clearDepth = function() {
  return this.setDepth(undefined);
};


/**
 * Returns whether this field is set.
 * @return {boolean}
 */
proto.secwager.Instrument.prototype.hasDepth = function() {
  return jspb.Message.getField(this, 6) != null;
};


/**
 * optional uint64 start_time_epoch_seconds = 7;
 * @return {number}
 */
proto.secwager.Instrument.prototype.getStartTimeEpochSeconds = function() {
  return /** @type {number} */ (jspb.Message.getFieldWithDefault(this, 7, 0));
};


/**
 * @param {number} value
 * @return {!proto.secwager.Instrument} returns this
 */
proto.secwager.Instrument.prototype.setStartTimeEpochSeconds = function(value) {
  return jspb.Message.setProto3IntField(this, 7, value);
};





if (jspb.Message.GENERATE_TO_OBJECT) {
/**
 * Creates an object representation of this proto.
 * Field names that are reserved in JavaScript and will be renamed to pb_name.
 * Optional fields that are not set will be set to undefined.
 * To access a reserved field use, foo.pb_<name>, eg, foo.pb_default.
 * For the list of reserved names please see:
 *     net/proto2/compiler/js/internal/generator.cc#kKeyword.
 * @param {boolean=} opt_includeInstance Deprecated. whether to include the
 *     JSPB instance for transitional soy proto support:
 *     http://goto/soy-param-migration
 * @return {!Object}
 */
proto.secwager.MarketDataRequest.prototype.toObject = function(opt_includeInstance) {
  return proto.secwager.MarketDataRequest.toObject(opt_includeInstance, this);
};


/**
 * Static version of the {@see toObject} method.
 * @param {boolean|undefined} includeInstance Deprecated. Whether to include
 *     the JSPB instance for transitional soy proto support:
 *     http://goto/soy-param-migration
 * @param {!proto.secwager.MarketDataRequest} msg The msg instance to transform.
 * @return {!Object}
 * @suppress {unusedLocalVariables} f is only used for nested messages
 */
proto.secwager.MarketDataRequest.toObject = function(includeInstance, msg) {
  var f, obj = {
    league: jspb.Message.getFieldWithDefault(msg, 1, 0)
  };

  if (includeInstance) {
    obj.$jspbMessageInstance = msg;
  }
  return obj;
};
}


/**
 * Deserializes binary data (in protobuf wire format).
 * @param {jspb.ByteSource} bytes The bytes to deserialize.
 * @return {!proto.secwager.MarketDataRequest}
 */
proto.secwager.MarketDataRequest.deserializeBinary = function(bytes) {
  var reader = new jspb.BinaryReader(bytes);
  var msg = new proto.secwager.MarketDataRequest;
  return proto.secwager.MarketDataRequest.deserializeBinaryFromReader(msg, reader);
};


/**
 * Deserializes binary data (in protobuf wire format) from the
 * given reader into the given message object.
 * @param {!proto.secwager.MarketDataRequest} msg The message object to deserialize into.
 * @param {!jspb.BinaryReader} reader The BinaryReader to use.
 * @return {!proto.secwager.MarketDataRequest}
 */
proto.secwager.MarketDataRequest.deserializeBinaryFromReader = function(msg, reader) {
  while (reader.nextField()) {
    if (reader.isEndGroup()) {
      break;
    }
    var field = reader.getFieldNumber();
    switch (field) {
    case 1:
      var value = /** @type {!proto.secwager.League} */ (reader.readEnum());
      msg.setLeague(value);
      break;
    default:
      reader.skipField();
      break;
    }
  }
  return msg;
};


/**
 * Serializes the message to binary data (in protobuf wire format).
 * @return {!Uint8Array}
 */
proto.secwager.MarketDataRequest.prototype.serializeBinary = function() {
  var writer = new jspb.BinaryWriter();
  proto.secwager.MarketDataRequest.serializeBinaryToWriter(this, writer);
  return writer.getResultBuffer();
};


/**
 * Serializes the given message to binary data (in protobuf wire
 * format), writing to the given BinaryWriter.
 * @param {!proto.secwager.MarketDataRequest} message
 * @param {!jspb.BinaryWriter} writer
 * @suppress {unusedLocalVariables} f is only used for nested messages
 */
proto.secwager.MarketDataRequest.serializeBinaryToWriter = function(message, writer) {
  var f = undefined;
  f = message.getLeague();
  if (f !== 0.0) {
    writer.writeEnum(
      1,
      f
    );
  }
};


/**
 * optional League league = 1;
 * @return {!proto.secwager.League}
 */
proto.secwager.MarketDataRequest.prototype.getLeague = function() {
  return /** @type {!proto.secwager.League} */ (jspb.Message.getFieldWithDefault(this, 1, 0));
};


/**
 * @param {!proto.secwager.League} value
 * @return {!proto.secwager.MarketDataRequest} returns this
 */
proto.secwager.MarketDataRequest.prototype.setLeague = function(value) {
  return jspb.Message.setProto3EnumField(this, 1, value);
};



/**
 * List of repeated fields within this message type.
 * @private {!Array<number>}
 * @const
 */
proto.secwager.MarketDataResponse.repeatedFields_ = [1];



if (jspb.Message.GENERATE_TO_OBJECT) {
/**
 * Creates an object representation of this proto.
 * Field names that are reserved in JavaScript and will be renamed to pb_name.
 * Optional fields that are not set will be set to undefined.
 * To access a reserved field use, foo.pb_<name>, eg, foo.pb_default.
 * For the list of reserved names please see:
 *     net/proto2/compiler/js/internal/generator.cc#kKeyword.
 * @param {boolean=} opt_includeInstance Deprecated. whether to include the
 *     JSPB instance for transitional soy proto support:
 *     http://goto/soy-param-migration
 * @return {!Object}
 */
proto.secwager.MarketDataResponse.prototype.toObject = function(opt_includeInstance) {
  return proto.secwager.MarketDataResponse.toObject(opt_includeInstance, this);
};


/**
 * Static version of the {@see toObject} method.
 * @param {boolean|undefined} includeInstance Deprecated. Whether to include
 *     the JSPB instance for transitional soy proto support:
 *     http://goto/soy-param-migration
 * @param {!proto.secwager.MarketDataResponse} msg The msg instance to transform.
 * @return {!Object}
 * @suppress {unusedLocalVariables} f is only used for nested messages
 */
proto.secwager.MarketDataResponse.toObject = function(includeInstance, msg) {
  var f, obj = {
    instrumentsList: jspb.Message.toObjectList(msg.getInstrumentsList(),
    proto.secwager.Instrument.toObject, includeInstance)
  };

  if (includeInstance) {
    obj.$jspbMessageInstance = msg;
  }
  return obj;
};
}


/**
 * Deserializes binary data (in protobuf wire format).
 * @param {jspb.ByteSource} bytes The bytes to deserialize.
 * @return {!proto.secwager.MarketDataResponse}
 */
proto.secwager.MarketDataResponse.deserializeBinary = function(bytes) {
  var reader = new jspb.BinaryReader(bytes);
  var msg = new proto.secwager.MarketDataResponse;
  return proto.secwager.MarketDataResponse.deserializeBinaryFromReader(msg, reader);
};


/**
 * Deserializes binary data (in protobuf wire format) from the
 * given reader into the given message object.
 * @param {!proto.secwager.MarketDataResponse} msg The message object to deserialize into.
 * @param {!jspb.BinaryReader} reader The BinaryReader to use.
 * @return {!proto.secwager.MarketDataResponse}
 */
proto.secwager.MarketDataResponse.deserializeBinaryFromReader = function(msg, reader) {
  while (reader.nextField()) {
    if (reader.isEndGroup()) {
      break;
    }
    var field = reader.getFieldNumber();
    switch (field) {
    case 1:
      var value = new proto.secwager.Instrument;
      reader.readMessage(value,proto.secwager.Instrument.deserializeBinaryFromReader);
      msg.addInstruments(value);
      break;
    default:
      reader.skipField();
      break;
    }
  }
  return msg;
};


/**
 * Serializes the message to binary data (in protobuf wire format).
 * @return {!Uint8Array}
 */
proto.secwager.MarketDataResponse.prototype.serializeBinary = function() {
  var writer = new jspb.BinaryWriter();
  proto.secwager.MarketDataResponse.serializeBinaryToWriter(this, writer);
  return writer.getResultBuffer();
};


/**
 * Serializes the given message to binary data (in protobuf wire
 * format), writing to the given BinaryWriter.
 * @param {!proto.secwager.MarketDataResponse} message
 * @param {!jspb.BinaryWriter} writer
 * @suppress {unusedLocalVariables} f is only used for nested messages
 */
proto.secwager.MarketDataResponse.serializeBinaryToWriter = function(message, writer) {
  var f = undefined;
  f = message.getInstrumentsList();
  if (f.length > 0) {
    writer.writeRepeatedMessage(
      1,
      f,
      proto.secwager.Instrument.serializeBinaryToWriter
    );
  }
};


/**
 * repeated Instrument instruments = 1;
 * @return {!Array<!proto.secwager.Instrument>}
 */
proto.secwager.MarketDataResponse.prototype.getInstrumentsList = function() {
  return /** @type{!Array<!proto.secwager.Instrument>} */ (
    jspb.Message.getRepeatedWrapperField(this, proto.secwager.Instrument, 1));
};


/**
 * @param {!Array<!proto.secwager.Instrument>} value
 * @return {!proto.secwager.MarketDataResponse} returns this
*/
proto.secwager.MarketDataResponse.prototype.setInstrumentsList = function(value) {
  return jspb.Message.setRepeatedWrapperField(this, 1, value);
};


/**
 * @param {!proto.secwager.Instrument=} opt_value
 * @param {number=} opt_index
 * @return {!proto.secwager.Instrument}
 */
proto.secwager.MarketDataResponse.prototype.addInstruments = function(opt_value, opt_index) {
  return jspb.Message.addToRepeatedWrapperField(this, 1, opt_value, proto.secwager.Instrument, opt_index);
};


/**
 * Clears the list making it empty but non-null.
 * @return {!proto.secwager.MarketDataResponse} returns this
 */
proto.secwager.MarketDataResponse.prototype.clearInstrumentsList = function() {
  return this.setInstrumentsList([]);
};


/**
 * @enum {number}
 */
proto.secwager.League = {
  LEAGUE_UNSPECIFIED: 0,
  ENGLISH_PREMIER_LEAGUE: 1,
  UEFA_CHAMPIONS_LEAGUE: 2,
  UEFA_EUROPA_LEAGUE: 3,
  LA_LIGA: 4,
  LIGUE_1: 5,
  SERIE_A: 6,
  EVERY_LEAGUE: 7,
  MAJOR_LEAGUE_SOCCER: 8
};

goog.object.extend(exports, proto.secwager);
