(function() {
    'use strict';
    angular
        .module('jhipsterApp')
        .factory('Person', Person);

    Person.$inject = ['$resource', 'DateUtils'];

    function Person ($resource, DateUtils) {
        var resourceUrl =  'api/people/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dateBorn = DateUtils.convertLocalDateFromServer(data.dateBorn);
                        data.dateCreated = DateUtils.convertDateTimeFromServer(data.dateCreated);
                        data.dateLastEdited = DateUtils.convertDateTimeFromServer(data.dateLastEdited);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.dateBorn = DateUtils.convertLocalDateToServer(data.dateBorn);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.dateBorn = DateUtils.convertLocalDateToServer(data.dateBorn);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
