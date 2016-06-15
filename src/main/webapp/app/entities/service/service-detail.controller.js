(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('ServiceDetailController', ServiceDetailController);

    ServiceDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Service', 'Fleet'];

    function ServiceDetailController($scope, $rootScope, $stateParams, entity, Service, Fleet) {
        var vm = this;

        vm.service = entity;

        var unsubscribe = $rootScope.$on('jhipsterApp:serviceUpdate', function(event, result) {
            vm.service = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
