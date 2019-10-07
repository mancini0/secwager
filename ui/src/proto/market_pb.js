// source: proto/market.proto
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

goog.exportSymbol('proto.secwager.DepthBook', null, global);
goog.exportSymbol('proto.secwager.LastTrade', null, global);
goog.exportSymbol('proto.secwager.Order', null, global);
goog.exportSymbol('proto.secwager.Order.OrderType', null, global);
goog.exportSymbol('proto.secwager.Order.State', null, global);
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
proto.secwager.Order = function(opt_data) {
  jspb.Message.initialize(this, opt_data, 0, -1, proto.secwager.Order.repeatedFields_, null);
};
goog.inherits(proto.secwager.Order, jspb.Message);
if (goog.DEBUG && !COMPILED) {
  /**
   * @public
   * @override
   */
  proto.secwager.Order.displayName = 'proto.secwager.Order';
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
proto.secwager.LastTrade = function(opt_data) {
  jspb.Message.initialize(this, opt_data, 0, -1, null, null);
};
goog.inherits(proto.secwager.LastTrade, jspb.Message);
if (goog.DEBUG && !COMPILED) {
  /**
   * @public
   * @override
   */
  proto.secwager.LastTrade.displayName = 'proto.secwager.LastTrade';
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
proto.secwager.DepthBook = function(opt_data) {
  jspb.Message.initialize(this, opt_data, 0, -1, proto.secwager.DepthBook.repeatedFields_, null);
};
goog.inherits(proto.secwager.DepthBook, jspb.Message);
if (goog.DEBUG && !COMPILED) {
  /**
   * @public
   * @override
   */
  proto.secwager.DepthBook.displayName = 'proto.secwager.DepthBook';
}

/**
 * List of repeated fields within this message type.
 * @private {!Array<number>}
 * @const
 */
proto.secwager.Order.repeatedFields_ = [16];



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
proto.secwager.Order.prototype.toObject = function(opt_includeInstance) {
  return proto.secwager.Order.toObject(opt_includeInstance, this);
};


/**
 * Static version of the {@see toObject} method.
 * @param {boolean|undefined} includeInstance Deprecated. Whether to include
 *     the JSPB instance for transitional soy proto support:
 *     http://goto/soy-param-migration
 * @param {!proto.secwager.Order} msg The msg instance to transform.
 * @return {!Object}
 * @suppress {unusedLocalVariables} f is only used for nested messages
 */
proto.secwager.Order.toObject = function(includeInstance, msg) {
  var f, obj = {
    orderId: jspb.Message.getFieldWithDefault(msg, 1, ""),
    orderType: jspb.Message.getFieldWithDefault(msg, 2, 0),
    symbol: jspb.Message.getFieldWithDefault(msg, 3, ""),
    isLimit: jspb.Message.getBooleanFieldWithDefault(msg, 4, false),
    price: jspb.Message.getFieldWithDefault(msg, 5, 0),
    stopPrice: jspb.Message.getFieldWithDefault(msg, 6, 0),
    orderQty: jspb.Message.getFieldWithDefault(msg, 7, 0),
    allOrNone: jspb.Message.getBooleanFieldWithDefault(msg, 8, false),
    immediateOrCancel: jspb.Message.getBooleanFieldWithDefault(msg, 9, false),
    state: jspb.Message.getFieldWithDefault(msg, 10, 0),
    rejectedReason: jspb.Message.getFieldWithDefault(msg, 11, ""),
    qtyFilled: jspb.Message.getFieldWithDefault(msg, 12, 0),
    fillCost: jspb.Message.getFieldWithDefault(msg, 13, 0),
    qtyOnMarket: jspb.Message.getFieldWithDefault(msg, 14, 0),
    originatingUserId: jspb.Message.getFieldWithDefault(msg, 15, ""),
    matchedAgainstList: (f = jspb.Message.getRepeatedField(msg, 16)) == null ? undefined : f,
    isBuy: jspb.Message.getBooleanFieldWithDefault(msg, 17, false)
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
 * @return {!proto.secwager.Order}
 */
proto.secwager.Order.deserializeBinary = function(bytes) {
  var reader = new jspb.BinaryReader(bytes);
  var msg = new proto.secwager.Order;
  return proto.secwager.Order.deserializeBinaryFromReader(msg, reader);
};


/**
 * Deserializes binary data (in protobuf wire format) from the
 * given reader into the given message object.
 * @param {!proto.secwager.Order} msg The message object to deserialize into.
 * @param {!jspb.BinaryReader} reader The BinaryReader to use.
 * @return {!proto.secwager.Order}
 */
proto.secwager.Order.deserializeBinaryFromReader = function(msg, reader) {
  while (reader.nextField()) {
    if (reader.isEndGroup()) {
      break;
    }
    var field = reader.getFieldNumber();
    switch (field) {
    case 1:
      var value = /** @type {string} */ (reader.readString());
      msg.setOrderId(value);
      break;
    case 2:
      var value = /** @type {!proto.secwager.Order.OrderType} */ (reader.readEnum());
      msg.setOrderType(value);
      break;
    case 3:
      var value = /** @type {string} */ (reader.readString());
      msg.setSymbol(value);
      break;
    case 4:
      var value = /** @type {boolean} */ (reader.readBool());
      msg.setIsLimit(value);
      break;
    case 5:
      var value = /** @type {number} */ (reader.readUint32());
      msg.setPrice(value);
      break;
    case 6:
      var value = /** @type {number} */ (reader.readUint32());
      msg.setStopPrice(value);
      break;
    case 7:
      var value = /** @type {number} */ (reader.readUint32());
      msg.setOrderQty(value);
      break;
    case 8:
      var value = /** @type {boolean} */ (reader.readBool());
      msg.setAllOrNone(value);
      break;
    case 9:
      var value = /** @type {boolean} */ (reader.readBool());
      msg.setImmediateOrCancel(value);
      break;
    case 10:
      var value = /** @type {!proto.secwager.Order.State} */ (reader.readEnum());
      msg.setState(value);
      break;
    case 11:
      var value = /** @type {string} */ (reader.readString());
      msg.setRejectedReason(value);
      break;
    case 12:
      var value = /** @type {number} */ (reader.readUint32());
      msg.setQtyFilled(value);
      break;
    case 13:
      var value = /** @type {number} */ (reader.readUint32());
      msg.setFillCost(value);
      break;
    case 14:
      var value = /** @type {number} */ (reader.readUint32());
      msg.setQtyOnMarket(value);
      break;
    case 15:
      var value = /** @type {string} */ (reader.readString());
      msg.setOriginatingUserId(value);
      break;
    case 16:
      var value = /** @type {string} */ (reader.readString());
      msg.addMatchedAgainst(value);
      break;
    case 17:
      var value = /** @type {boolean} */ (reader.readBool());
      msg.setIsBuy(value);
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
proto.secwager.Order.prototype.serializeBinary = function() {
  var writer = new jspb.BinaryWriter();
  proto.secwager.Order.serializeBinaryToWriter(this, writer);
  return writer.getResultBuffer();
};


/**
 * Serializes the given message to binary data (in protobuf wire
 * format), writing to the given BinaryWriter.
 * @param {!proto.secwager.Order} message
 * @param {!jspb.BinaryWriter} writer
 * @suppress {unusedLocalVariables} f is only used for nested messages
 */
proto.secwager.Order.serializeBinaryToWriter = function(message, writer) {
  var f = undefined;
  f = message.getOrderId();
  if (f.length > 0) {
    writer.writeString(
      1,
      f
    );
  }
  f = message.getOrderType();
  if (f !== 0.0) {
    writer.writeEnum(
      2,
      f
    );
  }
  f = message.getSymbol();
  if (f.length > 0) {
    writer.writeString(
      3,
      f
    );
  }
  f = message.getIsLimit();
  if (f) {
    writer.writeBool(
      4,
      f
    );
  }
  f = message.getPrice();
  if (f !== 0) {
    writer.writeUint32(
      5,
      f
    );
  }
  f = message.getStopPrice();
  if (f !== 0) {
    writer.writeUint32(
      6,
      f
    );
  }
  f = message.getOrderQty();
  if (f !== 0) {
    writer.writeUint32(
      7,
      f
    );
  }
  f = message.getAllOrNone();
  if (f) {
    writer.writeBool(
      8,
      f
    );
  }
  f = message.getImmediateOrCancel();
  if (f) {
    writer.writeBool(
      9,
      f
    );
  }
  f = message.getState();
  if (f !== 0.0) {
    writer.writeEnum(
      10,
      f
    );
  }
  f = message.getRejectedReason();
  if (f.length > 0) {
    writer.writeString(
      11,
      f
    );
  }
  f = message.getQtyFilled();
  if (f !== 0) {
    writer.writeUint32(
      12,
      f
    );
  }
  f = message.getFillCost();
  if (f !== 0) {
    writer.writeUint32(
      13,
      f
    );
  }
  f = message.getQtyOnMarket();
  if (f !== 0) {
    writer.writeUint32(
      14,
      f
    );
  }
  f = message.getOriginatingUserId();
  if (f.length > 0) {
    writer.writeString(
      15,
      f
    );
  }
  f = message.getMatchedAgainstList();
  if (f.length > 0) {
    writer.writeRepeatedString(
      16,
      f
    );
  }
  f = message.getIsBuy();
  if (f) {
    writer.writeBool(
      17,
      f
    );
  }
};


/**
 * @enum {number}
 */
proto.secwager.Order.OrderType = {
  BUY: 0,
  SELL: 1,
  CANCEL: 2,
  MODIFY: 3
};

/**
 * @enum {number}
 */
proto.secwager.Order.State = {
  SUBMITTED: 0,
  REJECTED: 1,
  ACCEPTED: 2,
  MODIFY_REQUESTED: 3,
  MODIFY_REJECTED: 4,
  MODIFIED: 5,
  PARTIAL_FILLED: 6,
  FILLED: 7,
  CANCEL_REQUESTED: 8,
  CANCEL_REJECTED: 9,
  CANCELLED: 10,
  UNKNOWN: 11
};

/**
 * optional string order_id = 1;
 * @return {string}
 */
proto.secwager.Order.prototype.getOrderId = function() {
  return /** @type {string} */ (jspb.Message.getFieldWithDefault(this, 1, ""));
};


/**
 * @param {string} value
 * @return {!proto.secwager.Order} returns this
 */
proto.secwager.Order.prototype.setOrderId = function(value) {
  return jspb.Message.setProto3StringField(this, 1, value);
};


/**
 * optional OrderType order_type = 2;
 * @return {!proto.secwager.Order.OrderType}
 */
proto.secwager.Order.prototype.getOrderType = function() {
  return /** @type {!proto.secwager.Order.OrderType} */ (jspb.Message.getFieldWithDefault(this, 2, 0));
};


/**
 * @param {!proto.secwager.Order.OrderType} value
 * @return {!proto.secwager.Order} returns this
 */
proto.secwager.Order.prototype.setOrderType = function(value) {
  return jspb.Message.setProto3EnumField(this, 2, value);
};


/**
 * optional string symbol = 3;
 * @return {string}
 */
proto.secwager.Order.prototype.getSymbol = function() {
  return /** @type {string} */ (jspb.Message.getFieldWithDefault(this, 3, ""));
};


/**
 * @param {string} value
 * @return {!proto.secwager.Order} returns this
 */
proto.secwager.Order.prototype.setSymbol = function(value) {
  return jspb.Message.setProto3StringField(this, 3, value);
};


/**
 * optional bool is_limit = 4;
 * @return {boolean}
 */
proto.secwager.Order.prototype.getIsLimit = function() {
  return /** @type {boolean} */ (jspb.Message.getBooleanFieldWithDefault(this, 4, false));
};


/**
 * @param {boolean} value
 * @return {!proto.secwager.Order} returns this
 */
proto.secwager.Order.prototype.setIsLimit = function(value) {
  return jspb.Message.setProto3BooleanField(this, 4, value);
};


/**
 * optional uint32 price = 5;
 * @return {number}
 */
proto.secwager.Order.prototype.getPrice = function() {
  return /** @type {number} */ (jspb.Message.getFieldWithDefault(this, 5, 0));
};


/**
 * @param {number} value
 * @return {!proto.secwager.Order} returns this
 */
proto.secwager.Order.prototype.setPrice = function(value) {
  return jspb.Message.setProto3IntField(this, 5, value);
};


/**
 * optional uint32 stop_price = 6;
 * @return {number}
 */
proto.secwager.Order.prototype.getStopPrice = function() {
  return /** @type {number} */ (jspb.Message.getFieldWithDefault(this, 6, 0));
};


/**
 * @param {number} value
 * @return {!proto.secwager.Order} returns this
 */
proto.secwager.Order.prototype.setStopPrice = function(value) {
  return jspb.Message.setProto3IntField(this, 6, value);
};


/**
 * optional uint32 order_qty = 7;
 * @return {number}
 */
proto.secwager.Order.prototype.getOrderQty = function() {
  return /** @type {number} */ (jspb.Message.getFieldWithDefault(this, 7, 0));
};


/**
 * @param {number} value
 * @return {!proto.secwager.Order} returns this
 */
proto.secwager.Order.prototype.setOrderQty = function(value) {
  return jspb.Message.setProto3IntField(this, 7, value);
};


/**
 * optional bool all_or_none = 8;
 * @return {boolean}
 */
proto.secwager.Order.prototype.getAllOrNone = function() {
  return /** @type {boolean} */ (jspb.Message.getBooleanFieldWithDefault(this, 8, false));
};


/**
 * @param {boolean} value
 * @return {!proto.secwager.Order} returns this
 */
proto.secwager.Order.prototype.setAllOrNone = function(value) {
  return jspb.Message.setProto3BooleanField(this, 8, value);
};


/**
 * optional bool immediate_or_cancel = 9;
 * @return {boolean}
 */
proto.secwager.Order.prototype.getImmediateOrCancel = function() {
  return /** @type {boolean} */ (jspb.Message.getBooleanFieldWithDefault(this, 9, false));
};


/**
 * @param {boolean} value
 * @return {!proto.secwager.Order} returns this
 */
proto.secwager.Order.prototype.setImmediateOrCancel = function(value) {
  return jspb.Message.setProto3BooleanField(this, 9, value);
};


/**
 * optional State state = 10;
 * @return {!proto.secwager.Order.State}
 */
proto.secwager.Order.prototype.getState = function() {
  return /** @type {!proto.secwager.Order.State} */ (jspb.Message.getFieldWithDefault(this, 10, 0));
};


/**
 * @param {!proto.secwager.Order.State} value
 * @return {!proto.secwager.Order} returns this
 */
proto.secwager.Order.prototype.setState = function(value) {
  return jspb.Message.setProto3EnumField(this, 10, value);
};


/**
 * optional string rejected_reason = 11;
 * @return {string}
 */
proto.secwager.Order.prototype.getRejectedReason = function() {
  return /** @type {string} */ (jspb.Message.getFieldWithDefault(this, 11, ""));
};


/**
 * @param {string} value
 * @return {!proto.secwager.Order} returns this
 */
proto.secwager.Order.prototype.setRejectedReason = function(value) {
  return jspb.Message.setProto3StringField(this, 11, value);
};


/**
 * optional uint32 qty_filled = 12;
 * @return {number}
 */
proto.secwager.Order.prototype.getQtyFilled = function() {
  return /** @type {number} */ (jspb.Message.getFieldWithDefault(this, 12, 0));
};


/**
 * @param {number} value
 * @return {!proto.secwager.Order} returns this
 */
proto.secwager.Order.prototype.setQtyFilled = function(value) {
  return jspb.Message.setProto3IntField(this, 12, value);
};


/**
 * optional uint32 fill_cost = 13;
 * @return {number}
 */
proto.secwager.Order.prototype.getFillCost = function() {
  return /** @type {number} */ (jspb.Message.getFieldWithDefault(this, 13, 0));
};


/**
 * @param {number} value
 * @return {!proto.secwager.Order} returns this
 */
proto.secwager.Order.prototype.setFillCost = function(value) {
  return jspb.Message.setProto3IntField(this, 13, value);
};


/**
 * optional uint32 qty_on_market = 14;
 * @return {number}
 */
proto.secwager.Order.prototype.getQtyOnMarket = function() {
  return /** @type {number} */ (jspb.Message.getFieldWithDefault(this, 14, 0));
};


/**
 * @param {number} value
 * @return {!proto.secwager.Order} returns this
 */
proto.secwager.Order.prototype.setQtyOnMarket = function(value) {
  return jspb.Message.setProto3IntField(this, 14, value);
};


/**
 * optional string originating_user_id = 15;
 * @return {string}
 */
proto.secwager.Order.prototype.getOriginatingUserId = function() {
  return /** @type {string} */ (jspb.Message.getFieldWithDefault(this, 15, ""));
};


/**
 * @param {string} value
 * @return {!proto.secwager.Order} returns this
 */
proto.secwager.Order.prototype.setOriginatingUserId = function(value) {
  return jspb.Message.setProto3StringField(this, 15, value);
};


/**
 * repeated string matched_against = 16;
 * @return {!Array<string>}
 */
proto.secwager.Order.prototype.getMatchedAgainstList = function() {
  return /** @type {!Array<string>} */ (jspb.Message.getRepeatedField(this, 16));
};


/**
 * @param {!Array<string>} value
 * @return {!proto.secwager.Order} returns this
 */
proto.secwager.Order.prototype.setMatchedAgainstList = function(value) {
  return jspb.Message.setField(this, 16, value || []);
};


/**
 * @param {string} value
 * @param {number=} opt_index
 * @return {!proto.secwager.Order} returns this
 */
proto.secwager.Order.prototype.addMatchedAgainst = function(value, opt_index) {
  return jspb.Message.addToRepeatedField(this, 16, value, opt_index);
};


/**
 * Clears the list making it empty but non-null.
 * @return {!proto.secwager.Order} returns this
 */
proto.secwager.Order.prototype.clearMatchedAgainstList = function() {
  return this.setMatchedAgainstList([]);
};


/**
 * optional bool is_buy = 17;
 * @return {boolean}
 */
proto.secwager.Order.prototype.getIsBuy = function() {
  return /** @type {boolean} */ (jspb.Message.getBooleanFieldWithDefault(this, 17, false));
};


/**
 * @param {boolean} value
 * @return {!proto.secwager.Order} returns this
 */
proto.secwager.Order.prototype.setIsBuy = function(value) {
  return jspb.Message.setProto3BooleanField(this, 17, value);
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
proto.secwager.LastTrade.prototype.toObject = function(opt_includeInstance) {
  return proto.secwager.LastTrade.toObject(opt_includeInstance, this);
};


/**
 * Static version of the {@see toObject} method.
 * @param {boolean|undefined} includeInstance Deprecated. Whether to include
 *     the JSPB instance for transitional soy proto support:
 *     http://goto/soy-param-migration
 * @param {!proto.secwager.LastTrade} msg The msg instance to transform.
 * @return {!Object}
 * @suppress {unusedLocalVariables} f is only used for nested messages
 */
proto.secwager.LastTrade.toObject = function(includeInstance, msg) {
  var f, obj = {
    symbol: jspb.Message.getFieldWithDefault(msg, 1, ""),
    price: jspb.Message.getFieldWithDefault(msg, 2, 0),
    qty: jspb.Message.getFieldWithDefault(msg, 3, 0)
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
 * @return {!proto.secwager.LastTrade}
 */
proto.secwager.LastTrade.deserializeBinary = function(bytes) {
  var reader = new jspb.BinaryReader(bytes);
  var msg = new proto.secwager.LastTrade;
  return proto.secwager.LastTrade.deserializeBinaryFromReader(msg, reader);
};


/**
 * Deserializes binary data (in protobuf wire format) from the
 * given reader into the given message object.
 * @param {!proto.secwager.LastTrade} msg The message object to deserialize into.
 * @param {!jspb.BinaryReader} reader The BinaryReader to use.
 * @return {!proto.secwager.LastTrade}
 */
proto.secwager.LastTrade.deserializeBinaryFromReader = function(msg, reader) {
  while (reader.nextField()) {
    if (reader.isEndGroup()) {
      break;
    }
    var field = reader.getFieldNumber();
    switch (field) {
    case 1:
      var value = /** @type {string} */ (reader.readString());
      msg.setSymbol(value);
      break;
    case 2:
      var value = /** @type {number} */ (reader.readUint32());
      msg.setPrice(value);
      break;
    case 3:
      var value = /** @type {number} */ (reader.readUint32());
      msg.setQty(value);
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
proto.secwager.LastTrade.prototype.serializeBinary = function() {
  var writer = new jspb.BinaryWriter();
  proto.secwager.LastTrade.serializeBinaryToWriter(this, writer);
  return writer.getResultBuffer();
};


/**
 * Serializes the given message to binary data (in protobuf wire
 * format), writing to the given BinaryWriter.
 * @param {!proto.secwager.LastTrade} message
 * @param {!jspb.BinaryWriter} writer
 * @suppress {unusedLocalVariables} f is only used for nested messages
 */
proto.secwager.LastTrade.serializeBinaryToWriter = function(message, writer) {
  var f = undefined;
  f = message.getSymbol();
  if (f.length > 0) {
    writer.writeString(
      1,
      f
    );
  }
  f = message.getPrice();
  if (f !== 0) {
    writer.writeUint32(
      2,
      f
    );
  }
  f = message.getQty();
  if (f !== 0) {
    writer.writeUint32(
      3,
      f
    );
  }
};


/**
 * optional string symbol = 1;
 * @return {string}
 */
proto.secwager.LastTrade.prototype.getSymbol = function() {
  return /** @type {string} */ (jspb.Message.getFieldWithDefault(this, 1, ""));
};


/**
 * @param {string} value
 * @return {!proto.secwager.LastTrade} returns this
 */
proto.secwager.LastTrade.prototype.setSymbol = function(value) {
  return jspb.Message.setProto3StringField(this, 1, value);
};


/**
 * optional uint32 price = 2;
 * @return {number}
 */
proto.secwager.LastTrade.prototype.getPrice = function() {
  return /** @type {number} */ (jspb.Message.getFieldWithDefault(this, 2, 0));
};


/**
 * @param {number} value
 * @return {!proto.secwager.LastTrade} returns this
 */
proto.secwager.LastTrade.prototype.setPrice = function(value) {
  return jspb.Message.setProto3IntField(this, 2, value);
};


/**
 * optional uint32 qty = 3;
 * @return {number}
 */
proto.secwager.LastTrade.prototype.getQty = function() {
  return /** @type {number} */ (jspb.Message.getFieldWithDefault(this, 3, 0));
};


/**
 * @param {number} value
 * @return {!proto.secwager.LastTrade} returns this
 */
proto.secwager.LastTrade.prototype.setQty = function(value) {
  return jspb.Message.setProto3IntField(this, 3, value);
};



/**
 * List of repeated fields within this message type.
 * @private {!Array<number>}
 * @const
 */
proto.secwager.DepthBook.repeatedFields_ = [2,3,4,5];



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
proto.secwager.DepthBook.prototype.toObject = function(opt_includeInstance) {
  return proto.secwager.DepthBook.toObject(opt_includeInstance, this);
};


/**
 * Static version of the {@see toObject} method.
 * @param {boolean|undefined} includeInstance Deprecated. Whether to include
 *     the JSPB instance for transitional soy proto support:
 *     http://goto/soy-param-migration
 * @param {!proto.secwager.DepthBook} msg The msg instance to transform.
 * @return {!Object}
 * @suppress {unusedLocalVariables} f is only used for nested messages
 */
proto.secwager.DepthBook.toObject = function(includeInstance, msg) {
  var f, obj = {
    symbol: jspb.Message.getFieldWithDefault(msg, 1, ""),
    bidPricesList: (f = jspb.Message.getRepeatedField(msg, 2)) == null ? undefined : f,
    bidQtysList: (f = jspb.Message.getRepeatedField(msg, 3)) == null ? undefined : f,
    askPricesList: (f = jspb.Message.getRepeatedField(msg, 4)) == null ? undefined : f,
    askQtysList: (f = jspb.Message.getRepeatedField(msg, 5)) == null ? undefined : f
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
 * @return {!proto.secwager.DepthBook}
 */
proto.secwager.DepthBook.deserializeBinary = function(bytes) {
  var reader = new jspb.BinaryReader(bytes);
  var msg = new proto.secwager.DepthBook;
  return proto.secwager.DepthBook.deserializeBinaryFromReader(msg, reader);
};


/**
 * Deserializes binary data (in protobuf wire format) from the
 * given reader into the given message object.
 * @param {!proto.secwager.DepthBook} msg The message object to deserialize into.
 * @param {!jspb.BinaryReader} reader The BinaryReader to use.
 * @return {!proto.secwager.DepthBook}
 */
proto.secwager.DepthBook.deserializeBinaryFromReader = function(msg, reader) {
  while (reader.nextField()) {
    if (reader.isEndGroup()) {
      break;
    }
    var field = reader.getFieldNumber();
    switch (field) {
    case 1:
      var value = /** @type {string} */ (reader.readString());
      msg.setSymbol(value);
      break;
    case 2:
      var value = /** @type {!Array<number>} */ (reader.readPackedUint32());
      msg.setBidPricesList(value);
      break;
    case 3:
      var value = /** @type {!Array<number>} */ (reader.readPackedUint32());
      msg.setBidQtysList(value);
      break;
    case 4:
      var value = /** @type {!Array<number>} */ (reader.readPackedUint32());
      msg.setAskPricesList(value);
      break;
    case 5:
      var value = /** @type {!Array<number>} */ (reader.readPackedUint32());
      msg.setAskQtysList(value);
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
proto.secwager.DepthBook.prototype.serializeBinary = function() {
  var writer = new jspb.BinaryWriter();
  proto.secwager.DepthBook.serializeBinaryToWriter(this, writer);
  return writer.getResultBuffer();
};


/**
 * Serializes the given message to binary data (in protobuf wire
 * format), writing to the given BinaryWriter.
 * @param {!proto.secwager.DepthBook} message
 * @param {!jspb.BinaryWriter} writer
 * @suppress {unusedLocalVariables} f is only used for nested messages
 */
proto.secwager.DepthBook.serializeBinaryToWriter = function(message, writer) {
  var f = undefined;
  f = message.getSymbol();
  if (f.length > 0) {
    writer.writeString(
      1,
      f
    );
  }
  f = message.getBidPricesList();
  if (f.length > 0) {
    writer.writePackedUint32(
      2,
      f
    );
  }
  f = message.getBidQtysList();
  if (f.length > 0) {
    writer.writePackedUint32(
      3,
      f
    );
  }
  f = message.getAskPricesList();
  if (f.length > 0) {
    writer.writePackedUint32(
      4,
      f
    );
  }
  f = message.getAskQtysList();
  if (f.length > 0) {
    writer.writePackedUint32(
      5,
      f
    );
  }
};


/**
 * optional string symbol = 1;
 * @return {string}
 */
proto.secwager.DepthBook.prototype.getSymbol = function() {
  return /** @type {string} */ (jspb.Message.getFieldWithDefault(this, 1, ""));
};


/**
 * @param {string} value
 * @return {!proto.secwager.DepthBook} returns this
 */
proto.secwager.DepthBook.prototype.setSymbol = function(value) {
  return jspb.Message.setProto3StringField(this, 1, value);
};


/**
 * repeated uint32 bid_prices = 2;
 * @return {!Array<number>}
 */
proto.secwager.DepthBook.prototype.getBidPricesList = function() {
  return /** @type {!Array<number>} */ (jspb.Message.getRepeatedField(this, 2));
};


/**
 * @param {!Array<number>} value
 * @return {!proto.secwager.DepthBook} returns this
 */
proto.secwager.DepthBook.prototype.setBidPricesList = function(value) {
  return jspb.Message.setField(this, 2, value || []);
};


/**
 * @param {number} value
 * @param {number=} opt_index
 * @return {!proto.secwager.DepthBook} returns this
 */
proto.secwager.DepthBook.prototype.addBidPrices = function(value, opt_index) {
  return jspb.Message.addToRepeatedField(this, 2, value, opt_index);
};


/**
 * Clears the list making it empty but non-null.
 * @return {!proto.secwager.DepthBook} returns this
 */
proto.secwager.DepthBook.prototype.clearBidPricesList = function() {
  return this.setBidPricesList([]);
};


/**
 * repeated uint32 bid_qtys = 3;
 * @return {!Array<number>}
 */
proto.secwager.DepthBook.prototype.getBidQtysList = function() {
  return /** @type {!Array<number>} */ (jspb.Message.getRepeatedField(this, 3));
};


/**
 * @param {!Array<number>} value
 * @return {!proto.secwager.DepthBook} returns this
 */
proto.secwager.DepthBook.prototype.setBidQtysList = function(value) {
  return jspb.Message.setField(this, 3, value || []);
};


/**
 * @param {number} value
 * @param {number=} opt_index
 * @return {!proto.secwager.DepthBook} returns this
 */
proto.secwager.DepthBook.prototype.addBidQtys = function(value, opt_index) {
  return jspb.Message.addToRepeatedField(this, 3, value, opt_index);
};


/**
 * Clears the list making it empty but non-null.
 * @return {!proto.secwager.DepthBook} returns this
 */
proto.secwager.DepthBook.prototype.clearBidQtysList = function() {
  return this.setBidQtysList([]);
};


/**
 * repeated uint32 ask_prices = 4;
 * @return {!Array<number>}
 */
proto.secwager.DepthBook.prototype.getAskPricesList = function() {
  return /** @type {!Array<number>} */ (jspb.Message.getRepeatedField(this, 4));
};


/**
 * @param {!Array<number>} value
 * @return {!proto.secwager.DepthBook} returns this
 */
proto.secwager.DepthBook.prototype.setAskPricesList = function(value) {
  return jspb.Message.setField(this, 4, value || []);
};


/**
 * @param {number} value
 * @param {number=} opt_index
 * @return {!proto.secwager.DepthBook} returns this
 */
proto.secwager.DepthBook.prototype.addAskPrices = function(value, opt_index) {
  return jspb.Message.addToRepeatedField(this, 4, value, opt_index);
};


/**
 * Clears the list making it empty but non-null.
 * @return {!proto.secwager.DepthBook} returns this
 */
proto.secwager.DepthBook.prototype.clearAskPricesList = function() {
  return this.setAskPricesList([]);
};


/**
 * repeated uint32 ask_qtys = 5;
 * @return {!Array<number>}
 */
proto.secwager.DepthBook.prototype.getAskQtysList = function() {
  return /** @type {!Array<number>} */ (jspb.Message.getRepeatedField(this, 5));
};


/**
 * @param {!Array<number>} value
 * @return {!proto.secwager.DepthBook} returns this
 */
proto.secwager.DepthBook.prototype.setAskQtysList = function(value) {
  return jspb.Message.setField(this, 5, value || []);
};


/**
 * @param {number} value
 * @param {number=} opt_index
 * @return {!proto.secwager.DepthBook} returns this
 */
proto.secwager.DepthBook.prototype.addAskQtys = function(value, opt_index) {
  return jspb.Message.addToRepeatedField(this, 5, value, opt_index);
};


/**
 * Clears the list making it empty but non-null.
 * @return {!proto.secwager.DepthBook} returns this
 */
proto.secwager.DepthBook.prototype.clearAskQtysList = function() {
  return this.setAskQtysList([]);
};


goog.object.extend(exports, proto.secwager);
