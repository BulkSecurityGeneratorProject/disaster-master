(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('MissionDeleteController',MissionDeleteController);

    MissionDeleteController.$inject = ['$uibModalInstance', 'entity', 'Mission'];

    function MissionDeleteController($uibModalInstance, entity, Mission) {
        var vm = this;

        vm.mission = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Mission.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
