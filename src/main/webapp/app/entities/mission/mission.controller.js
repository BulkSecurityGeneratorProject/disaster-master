(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('MissionController', MissionController);

    MissionController.$inject = ['$scope', '$state', 'Mission'];

    function MissionController ($scope, $state, Mission) {
        var vm = this;
        
        vm.missions = [];

        loadAll();

        function loadAll() {
            Mission.query(function(result) {
                vm.missions = result;
            });
        }
    }
})();
