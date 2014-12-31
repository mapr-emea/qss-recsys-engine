"use strict";
angular.module("myApp.filters", [])
.filter("partition", function() {
  var cache, _evenRows, _filter, _toHorizontalOrderMatrix, _toVerticalOrderMatrix;

  _evenRows = function(arr, cols) {
    var emptySlots, rows, rowsArr, slot, _i;
    rowsArr = _.clone(arr);
    rows = Math.ceil(rowsArr.length / cols);
    emptySlots = cols - (arr.length % cols);
    if ((emptySlots % cols) !== 0) {
      for (slot = _i = 1; 1 <= emptySlots ? _i <= emptySlots : _i >= emptySlots; slot = 1 <= emptySlots ? ++_i : --_i) {
        rowsArr.splice(((cols - emptySlots + slot) * rows) - 1, 0, null);
      }
    }
    return rowsArr;
  };
  _toHorizontalOrderMatrix = function(arr, cols) {
    var el, i, row, rowArr, rows, rowsArr, _i, _j, _len;
    rowsArr = [];
    arr = _evenRows(arr, cols);
    rows = Math.ceil(arr.length / cols);
    for (row = _i = 1; 1 <= rows ? _i <= rows : _i >= rows; row = 1 <= rows ? ++_i : --_i) {
      rowArr = [];
      for (i = _j = 0, _len = arr.length; _j < _len; i = ++_j) {
        el = arr[i];
        if (((i + 1) % rows) === row) {
          rowArr.push(el);
        } else if ((((i + 1) % rows) === 0) && (row === rows)) {
          rowArr.push(el);
        }
      }
      rowsArr.push(_.compact(rowArr));
    }
    return rowsArr;
  };
  _toVerticalOrderMatrix = function(arr, cols) {
    var i, rowsArr;
    rowsArr = [];
    i = 0;
    while (i < arr.length) {
      rowsArr.push(arr.slice(i, i + cols));
      i += cols;
    }
    return rowsArr;
  };
  cache = {};
  _filter = function(arr, size, hOrder) {
    var arrString, fromCache, horizontalOrder, rowsArr;
    if (!arr) {
      return;
    }
    horizontalOrder = true;
    if (typeof hOrder !== "undefined") {
      horizontalOrder = hOrder;
    }
    rowsArr = [];
    if (horizontalOrder) {
      rowsArr = _toHorizontalOrderMatrix(arr, size);
    } else {
      rowsArr = _toVerticalOrderMatrix(arr, size);
    }
    arrString = JSON.stringify(arr);
    fromCache = cache[arrString + size];
    if (JSON.stringify(fromCache) === JSON.stringify(rowsArr)) {
      return fromCache;
    }
    cache[arrString + size] = rowsArr;
    return rowsArr;
  };
  return _filter;
});