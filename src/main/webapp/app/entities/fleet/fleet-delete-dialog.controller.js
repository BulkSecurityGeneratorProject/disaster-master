(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('FleetDeleteController',FleetDeleteController);

    FleetDeleteController.$inject = ['$uibModalInstance', 'entity', 'Fleet'];

    function FleetDeleteController($uibModalInstance, entity, Fleet) {
        var vm = this;

        vm.fleet = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Fleet.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
