(function() {
    'use strict';
    angular
        .module('jhipsterApp')
        .factory('Mission', Mission);

    Mission.$inject = ['$resource', 'DateUtils'];

    function Mission ($resource, DateUtils) {
        var resourceUrl =  'api/missions/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dateStarted = DateUtils.convertDateTimeFromServer(data.dateStarted);
                        data.dateCompleted = DateUtils.convertDateTimeFromServer(data.dateCompleted);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
