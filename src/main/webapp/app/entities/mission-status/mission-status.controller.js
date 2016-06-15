(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('MissionStatusController', MissionStatusController);

    MissionStatusController.$inject = ['$scope', '$state', 'MissionStatus'];

    function MissionStatusController ($scope, $state, MissionStatus) {
        var vm = this;
        
        vm.missionStatuses = [];

        loadAll();

        function loadAll() {
            MissionStatus.query(function(result) {
                vm.missionStatuses = result;
            });
        }
    }
})();
