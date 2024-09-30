//
//  Generated code. Do not modify.
//  source: proto/main.proto
//
// @dart = 2.12

// ignore_for_file: annotate_overrides, camel_case_types, comment_references
// ignore_for_file: constant_identifier_names, library_prefixes
// ignore_for_file: non_constant_identifier_names, prefer_final_fields
// ignore_for_file: unnecessary_import, unnecessary_this, unused_import

import 'dart:core' as $core;

import 'package:protobuf/protobuf.dart' as $pb;

/// const names
class Requests extends $pb.ProtobufEnum {
  static const Requests REQUEST_NONE = Requests._(0, _omitEnumNames ? '' : 'REQUEST_NONE');
  static const Requests REQUEST_HISTORY_REQUEST = Requests._(1, _omitEnumNames ? '' : 'REQUEST_HISTORY_REQUEST');
  static const Requests REQUEST_HISTORY_RESPONSE = Requests._(2, _omitEnumNames ? '' : 'REQUEST_HISTORY_RESPONSE');

  static const $core.List<Requests> values = <Requests> [
    REQUEST_NONE,
    REQUEST_HISTORY_REQUEST,
    REQUEST_HISTORY_RESPONSE,
  ];

  static final $core.Map<$core.int, Requests> _byValue = $pb.ProtobufEnum.initByValue(values);
  static Requests? valueOf($core.int value) => _byValue[value];

  const Requests._($core.int v, $core.String n) : super(v, n);
}


const _omitEnumNames = $core.bool.fromEnvironment('protobuf.omit_enum_names');
