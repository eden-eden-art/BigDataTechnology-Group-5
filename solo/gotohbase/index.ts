var HBase = require('hbase-client');

var client = HBase.create({
    zookeeperHosts: [
        '127.0.0.1:2181'
    ],
    zookeeperRoot: '/hbase-0.94.16',
});

client.putRow('someTableName', 'rowkey1', { 'f1:name': 'foo name', 'f1:age': '18' }, function (err) {
    console.log(err);
});

client.getRow('someTableName', 'rowkey1', ['f1:name', 'f1:age'], function (err, row) {
    console.log(row);
});