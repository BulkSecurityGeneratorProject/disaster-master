(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('UnitController', UnitController);

    UnitController.$inject = ['$scope', '$state', 'Unit'];

    function UnitController ($scope, $state, Unit) {
        var vm = this;
        
        vm.units = [];

        loadAll();

        function loadAll() {
            Unit.query(function(result) {
                vm.units = result;
            });
        }
    }
})();
