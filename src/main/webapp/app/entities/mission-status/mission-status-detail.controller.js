(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('MissionStatusDetailController', MissionStatusDetailController);

    MissionStatusDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'MissionStatus', 'Mission'];

    function MissionStatusDetailController($scope, $rootScope, $stateParams, entity, MissionStatus, Mission) {
        var vm = this;

        vm.missionStatus = entity;

        var unsubscribe = $rootScope.$on('jhipsterApp:missionStatusUpdate', function(event, result) {
            vm.missionStatus = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
