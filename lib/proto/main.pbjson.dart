//
//  Generated code. Do not modify.
//  source: proto/main.proto
//
// @dart = 2.12

// ignore_for_file: annotate_overrides, camel_case_types, comment_references
// ignore_for_file: constant_identifier_names, library_prefixes
// ignore_for_file: non_constant_identifier_names, prefer_final_fields
// ignore_for_file: unnecessary_import, unnecessary_this, unused_import

import 'dart:convert' as $convert;
import 'dart:core' as $core;
import 'dart:typed_data' as $typed_data;

@$core.Deprecated('Use requestsDescriptor instead')
const Requests$json = {
  '1': 'Requests',
  '2': [
    {'1': 'REQUEST_NONE', '2': 0},
    {'1': 'REQUEST_HISTORY_REQUEST', '2': 1},
    {'1': 'REQUEST_HISTORY_RESPONSE', '2': 2},
  ],
};

/// Descriptor for `Requests`. Decode as a `google.protobuf.EnumDescriptorProto`.
final $typed_data.Uint8List requestsDescriptor = $convert.base64Decode(
    'CghSZXF1ZXN0cxIQCgxSRVFVRVNUX05PTkUQABIbChdSRVFVRVNUX0hJU1RPUllfUkVRVUVTVB'
    'ABEhwKGFJFUVVFU1RfSElTVE9SWV9SRVNQT05TRRAC');

@$core.Deprecated('Use historyDescriptor instead')
const History$json = {
  '1': 'History',
  '2': [
    {'1': 'action', '3': 1, '4': 1, '5': 9, '10': 'action'},
    {'1': 'flags', '3': 2, '4': 1, '5': 5, '10': 'flags'},
    {'1': 'data', '3': 3, '4': 1, '5': 9, '10': 'data'},
  ],
};

/// Descriptor for `History`. Decode as a `google.protobuf.DescriptorProto`.
final $typed_data.Uint8List historyDescriptor = $convert.base64Decode(
    'CgdIaXN0b3J5EhYKBmFjdGlvbhgBIAEoCVIGYWN0aW9uEhQKBWZsYWdzGAIgASgFUgVmbGFncx'
    'ISCgRkYXRhGAMgASgJUgRkYXRh');

@$core.Deprecated('Use historyRequestDescriptor instead')
const HistoryRequest$json = {
  '1': 'HistoryRequest',
};

/// Descriptor for `HistoryRequest`. Decode as a `google.protobuf.DescriptorProto`.
final $typed_data.Uint8List historyRequestDescriptor = $convert.base64Decode(
    'Cg5IaXN0b3J5UmVxdWVzdA==');

@$core.Deprecated('Use historyResponseDescriptor instead')
const HistoryResponse$json = {
  '1': 'HistoryResponse',
  '2': [
    {'1': 'history', '3': 1, '4': 3, '5': 11, '6': '.com.cebbinghaus.linkd.proto.History', '10': 'history'},
  ],
};

/// Descriptor for `HistoryResponse`. Decode as a `google.protobuf.DescriptorProto`.
final $typed_data.Uint8List historyResponseDescriptor = $convert.base64Decode(
    'Cg9IaXN0b3J5UmVzcG9uc2USPgoHaGlzdG9yeRgBIAMoCzIkLmNvbS5jZWJiaW5naGF1cy5saW'
    '5rZC5wcm90by5IaXN0b3J5UgdoaXN0b3J5');

