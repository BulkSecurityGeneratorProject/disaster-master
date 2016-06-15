(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('MissionDetailController', MissionDetailController);

    MissionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Mission', 'MissionStatus'];

    function MissionDetailController($scope, $rootScope, $stateParams, entity, Mission, MissionStatus) {
        var vm = this;

        vm.mission = entity;

        var unsubscribe = $rootScope.$on('jhipsterApp:missionUpdate', function(event, result) {
            vm.mission = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
