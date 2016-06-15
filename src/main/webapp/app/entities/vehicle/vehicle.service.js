(function() {
    'use strict';
    angular
        .module('jhipsterApp')
        .factory('Vehicle', Vehicle);

    Vehicle.$inject = ['$resource', 'DateUtils'];

    function Vehicle ($resource, DateUtils) {
        var resourceUrl =  'api/vehicles/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dateCreated = DateUtils.convertLocalDateFromServer(data.dateCreated);
                        data.dateLastAccessed = DateUtils.convertDateTimeFromServer(data.dateLastAccessed);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.dateCreated = DateUtils.convertLocalDateToServer(data.dateCreated);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.dateCreated = DateUtils.convertLocalDateToServer(data.dateCreated);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
