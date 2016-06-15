(function() {
    'use strict';
    angular
        .module('jhipsterApp')
        .factory('MissionStatus', MissionStatus);

    MissionStatus.$inject = ['$resource'];

    function MissionStatus ($resource) {
        var resourceUrl =  'api/mission-statuses/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
